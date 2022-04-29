from fastapi import FastAPI
import uvicorn
from models import Service
import random

services = {
    'agency': [],
    'college': []
}
app = FastAPI()


@app.post('/register')
async def register(service: Service):
    services[service.type].append(service)
    return {'message': 'Service registered successfully', 'success': True}


@app.post('/unregister')
async def unregister(service: Service):
    services[service.type].remove(service)
    return {'message': 'Service unregistered successfully', 'success': True}


@app.get('/service/{type}')
async def get_services(type: str):
    return random.choice(services[type])


if __name__ == '__main__':
    uvicorn.run('service_registry:app', host='127.0.0.1',
                port=9000, log_level='info')
