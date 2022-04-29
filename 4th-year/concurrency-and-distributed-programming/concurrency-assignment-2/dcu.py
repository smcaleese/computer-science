
import sys
from fastapi import FastAPI
import uvicorn
import os
from models import Proposal, AccountInfo, Service
import json

from utils import register_with_registry, unregister_with_registry
port = int(sys.argv[1])


def read_file(file_name):
    with open(f'./account_files/{file_name}.json') as file:
        return json.load(file)


def write_to_file(file_name, account_info):
    with open(f'./account_files/{file_name}.json', 'w') as account_file:
        json.dump(account_info, account_file, indent=4)


app = FastAPI()


@app.post('/account')
async def create_account_file(proposal: Proposal):
    try:
        script_path = os.path.dirname(os.path.realpath(__file__))
        if os.getcwd() == script_path:
            os.chdir('account_files')
        with open(f'{proposal.acronym}.json', 'w') as f:
            account_info = proposal.dict()
            del account_info['amount']
            account_info['balance'] = proposal.amount
            account_info['transactions'] = [
                f'Initial deposite of {proposal.amount} in account {proposal.acronym}.']
            json.dump(account_info, f, indent=4)
        os.chdir('..')
        return True
    except Exception:
        return False


@app.get('/account/{file_name}')
async def get_account_info(file_name):
    account_info = read_file(file_name)
    return account_info


@app.post('/account/{file_name}')
async def post_account_info(file_name, account_info: AccountInfo):
    write_to_file(file_name, account_info.dict())


@app.on_event('startup')
async def startup_event():
    service = Service(type='college', url='localhost', port=port)
    if not register_with_registry(service):
        print("Failed to register with registry")
        sys.exit(1)


@app.on_event('shutdown')
async def startup_event():
    service = Service(type='college', url='localhost', port=port)
    if not unregister_with_registry(service):
        print("Failed to deregister with registry")
        sys.exit(1)

if __name__ == '__main__':
    uvicorn.run('dcu:app', host='127.0.0.1', port=port, log_level='info')
