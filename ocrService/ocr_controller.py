import os
from fastapi import FastAPI
from paddleocr import PaddleOCR
from Models.OcrRequest import OcrRequest
from Models.OcrResponse import OcrResponse
import requests
import tempfile
import os

app = FastAPI()

ocr = PaddleOCR(
    use_doc_orientation_classify=False,
    use_doc_unwarping=False,
    use_textline_orientation=False)

@app.post("/api/ocr/scan")
def scan(request : OcrRequest):
    response = requests.get(request.image_link)
    if response.status_code != 200:
        raise Exception("Failed to download image")

    with tempfile.NamedTemporaryFile(delete=False, suffix=".jpg") as tmp_file:
        tmp_file.write(response.content)
        tmp_file_path = tmp_file.name

    try:
        result = ocr.predict(input=tmp_file_path)
        return OcrResponse(
            rec_texts=result[0]["rec_texts"],
            rec_polys=result[0]["rec_polys"]
        )
    finally:
        os.remove(tmp_file_path)
