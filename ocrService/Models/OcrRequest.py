from pydantic import BaseModel


class OcrRequest(BaseModel):
    image_link: str