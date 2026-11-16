import io

import numpy as np 
import pymupdf
from PIL import Image 
from rapidocr import RapidOCR

_engine = RapidOCR() #loads the model once, because loading is slow
MIN_TEXT_CHARS = 30

def _group_rows(boxes, txts, scores):
    items = []
    for box, text, score in zip(boxes, txts, scores):
        xs = [p[0] for p in box]
        ys = [p[1] for p in box]

        items.append({"x": min(xs), "y": sum(ys)/len(ys), "h": max(ys)-min(ys), "text": text})

    heights = sorted(i["h"] for i in items)
    row_gap = 0.7 * heights[len(heights) // 2]    #70% of typical height

    items.sort(key=lambda i:i["y"])
    rows = [[items[0]]]
    for item in items[1:]:
        row_y = sum(i["y"] for i in rows[-1]) / len(rows[-1])
        if abs(item["y"] - row_y) <= row_gap:
            rows[-1].append(item)
        else:
            rows.append([item])

    lines = []
    for row in rows:
        row.sort(key=lambda i: i["x"])
        lines.append("  ".join(i["text"] for i in row))
    return "\n".join(lines)


def _ocr_image(image):
    array = np.array(image.convert("RGB"))[:, :, ::-1]    # RapidOCR expects BGR
    result = _engine(array)
    if result.txts is None:
        return "", 0.0
    text = _group_rows(result.boxes, result.txts, result.scores)
    confidence = sum(result.scores) / len(result.scores)
    return text, round(float(confidence), 3)


def _page_to_image(page, dpi=200):
    pixmap = page.get_pixmap(dpi=dpi)
    return Image.open(io.BytesIO(pixmap.tobytes("png")))


def extract_pages(data: bytes, filename: str):
    pages = []

    if filename.lower().endswith(".pdf"):
        document = pymupdf.open(stream=data, filetype="pdf")
        for number, page in enumerate(document, start=1):
            text = page.get_text().strip()
            if len(text) >= MIN_TEXT_CHARS:
                pages.append({"page": number, "method": "text-layer", "confidence": 1.0, "text": text})
            else:
                ocr_text, confidence = _ocr_image(_page_to_image(page))
                pages.append({"page": number, "method": "ocr", "confidence": confidence, "text": ocr_text})
    else:
        image = Image.open(io.BytesIO(data))
        ocr_text, confidence = _ocr_image(image)
        pages.append({"page": 1, "method": "ocr", "confidence": confidence, "text": ocr_text})

    return pages
