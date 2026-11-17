from fastapi import FastAPI, File, HTTPException, UploadFile
from fastapi.concurrency import run_in_threadpool

from extract import extract_pages

app = FastAPI(title="SwasthyaQueue OCR Service") #it creates the web application, the python counterpart for spring app.

ALLOWED_EXTENSIONS = (".pdf", ".png", ".jpg", ".jpeg")
MAX_BYTES = 10 * 1024 * 1024  #10MB

@app.get("/health") #it is like @GetMapping...just kind of monitoring tool
def health():
    return{"status": "ok"}

@app.post("/extract") #It is like @PostMapping
#UploadFile = File(...): FastAPI’s way of saying “this endpoint receives an uploaded file”.
async def extract(file: UploadFile = File(...)):
    name = file.filename or ""
    if not name.lower().endswith(ALLOWED_EXTENSIONS):
        #it is kind of global excepion handler
        raise HTTPException(status_code=400, detail="Only PDF, PNG and JPG files are supported.")
    #below line means:- while the file is being received, the server may serve other requests meanwhile.
    data = await file.read()
    if not data:
        raise HTTPException(status_code=400, detail="The file is empty.")
    if len(data) > MAX_BYTES:
        raise HTTPException(status_code=413, detail="The file is larger than 10 MB.")

    try:
        #OCR is slow, CPU-heavy work, and running it directly inside an async function would freeze the whole server.
        pages = await run_in_threadpool(extract_pages, data, name)
    except Exception:
        raise HTTPException(status_code=422, detail="Could not read this file. It may be damaged.")

    return {"filename": name, "pages": pages}


