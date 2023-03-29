# YOLOv5 🚀 by Ultralytics, GPL-3.0 license
"""
Run YOLOv5 detection inference on images, videos, directories, globs, YouTube, webcam, streams, etc.

Usage - sources:
    $ python detect.py --weights yolov5s.pt --source 0                               # webcam
                                                     img.jpg                         # image
                                                     vid.mp4                         # video
                                                     screen                          # screenshot
                                                     path/                           # directory
                                                     list.txt                        # list of images
                                                     list.streams                    # list of streams
                                                     'path/*.jpg'                    # glob
                                                     'https://youtu.be/Zgi9g1ksQHc'  # YouTube
                                                     'rtsp://example.com/media.mp4'  # RTSP, RTMP, HTTP stream

Usage - formats:
    $ python detect.py --weights yolov5s.pt                 # PyTorch
                                 yolov5s.torchscript        # TorchScript
                                 yolov5s.onnx               # ONNX Runtime or OpenCV DNN with --dnn
                                 yolov5s_openvino_model     # OpenVINO
                                 yolov5s.engine             # TensorRT
                                 yolov5s.mlmodel            # CoreML (macOS-only)
                                 yolov5s_saved_model        # TensorFlow SavedModel
                                 yolov5s.pb                 # TensorFlow GraphDef
                                 yolov5s.tflite             # TensorFlow Lite
                                 yolov5s_edgetpu.tflite     # TensorFlow Edge TPU
                                 yolov5s_paddle_model       # PaddlePaddle
"""

import argparse
import os
import platform
import sys
from pathlib import Path

import torch


import json
from flask import Flask, Response, request, abort
from flask_cors import CORS, cross_origin
from io import BytesIO

import numpy as np

FILE = Path(__file__).resolve()
ROOT = FILE.parents[0]  # YOLOv5 root directory
if str(ROOT) not in sys.path:
    sys.path.append(str(ROOT))  # add ROOT to PATH
ROOT = Path(os.path.relpath(ROOT, Path.cwd()))  # relative

from models.common import DetectMultiBackend
from utils.dataloaders import IMG_FORMATS, VID_FORMATS, LoadScreenshots, LoadStreams
from utils.general import (LOGGER, Profile, check_file, check_img_size, check_imshow, check_requirements, colorstr, cv2,
                           increment_path, non_max_suppression, print_args, scale_boxes, strip_optimizer, xyxy2xywh)
from utils.plots import Annotator, colors, save_one_box
from utils.torch_utils import select_device, smart_inference_mode

app = Flask(__name__)
cors = CORS(app, resources={
    r"/*": {
        "origins": "*"
    }
})

@app.route('/flask/validate', methods=['POST'])
def getImage():
    # request.files 에서 파일 가져오기    
    file = request.files['image'].read()

    # 파일 없을 겨우
    #if not file:
    #    abort(415, 'No file uploaded')

    result = certifyAurora(file)
    if result == True:
        return Response(status=201, mimetype='application/json')
    else :
        return abort(400)


@smart_inference_mode()
def certifyAurora(image_data):
    weights = './weights/aurora.pt'
    data = './aurora.yaml'
    imgsz = (640, 640)
    conf_thres = 0.25  # confidence threshold
    iou_thres = 0.45  # NMS IOU threshold
    max_det = 1000  # maximum detections per image
    device = ''  # cuda device, i.e. 0 or 0,1,2,3 or cpu
    classes = None  # filter by class: --class 0, or --class 0 2 3
    agnostic_nms = False  # class-agnostic NMS
    augment = False  # augmented inference
    visualize = False  # visualize features
    half = False  # use FP16 half-precision inference
    dnn = False  # use OpenCV DNN for ONNX inference
    vid_stride = 1  # video frame-rate stride

    device = select_device(device)
    model = DetectMultiBackend(weights, device=device, dnn=dnn, data=data, fp16=half)
    stride, names, pt = model.stride, model.names, model.pt

    imgsz = check_img_size(imgsz, s=stride)  # check image size

    img = cv2.imdecode(np.frombuffer(image_data, dtype=np.uint8), cv2.IMREAD_COLOR)
    img = cv2.resize(img, imgsz)

    model.warmup(imgsz=(1 if pt or model.triton else bs, 3, *imgsz))  # warmup

    with torch.no_grad():

        im = cv2.resize(img, imgsz)
        im = cv2.cvtColor(im, cv2.COLOR_BGR2RGB)  # RGB로 변환
        im = im.transpose(2, 0, 1)  # 차원을 (높이, 너비, 채널)에서 (채널, 높이, 너비)로 변경
        im = torch.from_numpy(im).unsqueeze(0).to(model.device)  # 배치 차원 추가 및 torch 텐서로 변환
        im = im.half() if model.fp16 else im.float()  # uint8을 fp16/32로 변환
        im /= 255  # 0 - 255를 0.0 - 1.0으로 변환

        if len(im.shape) == 3:
            im = im[None]  # expand for batch dim

        # Inference
        visualize = increment_path(save_dir / Path(path).stem, mkdir=True) if visualize else False
        pred = model(im, augment=augment, visualize=visualize)

        # NMS 같은 물체를 여러번 탐색하는데 그 중 하나만 선택하는 과정
        pred = non_max_suppression(pred, conf_thres, iou_thres, classes, agnostic_nms, max_det=max_det)

    result = False
    for detection in pred[0]:
        if detection[4] > 0.4:
            result = True
            print(result)
            return result
    return result

if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0', port=8080)