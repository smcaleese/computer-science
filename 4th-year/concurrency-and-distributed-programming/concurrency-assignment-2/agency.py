from fastapi import FastAPI
import uvicorn
import requests
from models import Proposal, Service
import sys

from utils import get_server_of_type, unregister_with_registry, register_with_registry

app = FastAPI()
port = int(sys.argv[1])


@app.post('/proposal')
async def handle_proposal(proposal: Proposal):
    global dcu_server_endpoint
    if proposal.amount >= 200000 and proposal.amount <= 500000:
        account_created = requests.post(
            f"{get_server_of_type('college')}/account", json=proposal.dict())
        if account_created:
            return {'message': 'Proposal accepted, successfully created account', 'success': True}
        else:
            return {'message': 'Proposal accepted, failed to create account', 'success': False}
    else:
        return {'message': 'Proposal rejected', 'success': False}


@app.on_event('startup')
async def startup_event():
    service = Service(type='agency', url='localhost', port=port)
    if not register_with_registry(service):
        print("Failed to register with registry")
        sys.exit(1)


@app.on_event('shutdown')
async def startup_event():
    service = Service(type='agency', url='localhost', port=port)
    if not unregister_with_registry(service):
        print("Failed to deregister with registry")
        sys.exit(1)


if __name__ == '__main__':
    # get port from command line arg
    uvicorn.run('agency:app', host='127.0.0.1', port=port, log_level='info')
