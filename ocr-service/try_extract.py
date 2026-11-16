from pathlib import Path
from extract import extract_pages

for name in ["lab_report_digital.pdf", "lab_report_scan.pdf", "lab_report_scan.png"]:
    data = Path("samples", name).read_bytes()
    for page in extract_pages(data, name):
        print("=====", name, "| page", page["page"], "|", page["method"], "| conf", page["confidence"])
        print(page["text"])
        print()