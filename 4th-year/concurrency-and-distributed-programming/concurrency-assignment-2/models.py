from typing import Literal
from pydantic import BaseModel

class Proposal(BaseModel):
    acronym: str
    title: str
    description: str
    amount: int
    authorized_researchers: list
    days_left: int

class AccountInfo(BaseModel):
    acronym: str
    title: str
    description: str
    authorized_researchers: list
    days_left: int
    balance: int
    transactions: list

class Service(BaseModel):
    type: Literal['agency', 'college']
    url: str
    port: str
