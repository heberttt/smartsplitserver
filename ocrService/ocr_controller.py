from fastapi import FastAPI
from paddleocr import PaddleOCR
from Models.OcrRequest import OcrRequest
from Models.OcrResponse import OcrResponse

app = FastAPI()

ocr = PaddleOCR(
    use_doc_orientation_classify=False,
    use_doc_unwarping=False,
    use_textline_orientation=False)

@app.get("/hello")
def hello():
    return "hello world"


@app.post("/api/ocr")
def scan(request : OcrRequest):  # add async?
    result = ocr.predict(input=request.image_link)

    return OcrResponse(
        rec_texts=result[0]["rec_texts"],
        rec_polys=result[0]["rec_polys"]
    )
