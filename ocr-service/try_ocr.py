from rapidocr import RapidOCR   

engine = RapidOCR() 
result = engine("samples/lab_report_scan.png")

for text, score in zip(result.txts, result.scores):
    print(f"{score:.2f} {text}")