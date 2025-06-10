from pydantic import BaseModel
from typing import List


class OcrResponse(BaseModel):
    rec_texts : List[str]
    rec_polys : List[List[List[int]]]