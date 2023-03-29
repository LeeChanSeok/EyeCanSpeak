FROM python:3.8

WORKDIR /usr/src

COPY . .

RUN apt-get update && apt-get install -y libgl1-mesa-glx
RUN pip install --no-cache-dir --upgrade pip && \
    pip install -r requirements.txt
	
CMD ["aroAiServer.py"]

ENTRYPOINT ["python3"]