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
from flask import Flask
from flask_cors import CORS, cross_origin

FILE = Path(__file__).resolve()
ROOT = FILE.parents[0]  # YOLOv5 root directory
if str(ROOT) not in sys.path:
    sys.path.append(str(ROOT))  # add ROOT to PATH
ROOT = Path(os.path.relpath(ROOT, Path.cwd()))  # relative

from models.common import DetectMultiBackend
from utils.dataloaders import IMG_FORMATS, VID_FORMATS, LoadImages, LoadScreenshots, LoadStreams
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

# @app.route('/file', methods=['POST'])
# def getFile(fle=None):
#     if request.method == 'POST':
#         if 'file' not in request.files:
#             return 'File is missing', 404
#
#         pic_data = request.files['file']
#         filename = secure_filename(pic_data.filename)  # 업로드 된 파일의 이름이 안전한가를 확인해주는 함수이다. 해킹 공격에 대해 보안을 하고자 사용되기도 한다.
#         print(pic_data)
#         print(filename)
#     return 'ok'


@app.route('/certify', methods=['POST'])
def getImage():
    # request.files 에서 파일 가져오기    file = request.files['image'].read()

    # 파일 없을 겨우
    if not file:
        abort(400, 'No file uploaded')

    result = certifyAurora(file)

    return '{"result": {0} }'.format(result)
    
@smart_inference_mode()
def certifyAurora(image_data):
    weights='./weights/aurora.pt'
    data = './aurora.yaml'
    imgsz = (640, 640)
    conf_thres = 0.25  # confidence threshold
    iou_thres=0.45  # NMS IOU threshold
    max_det = 1000  # maximum detections per image
    device = ''  # cuda device, i.e. 0 or 0,1,2,3 or cpu
    view_img=False  # show results
    save_txt=False  # save results to *.txt
    save_conf=False  # save confidences in --save-txt labels
    save_crop=False  # save cropped prediction boxes
    nosave=False  # do not save images/videos
    classes=None  # filter by class: --class 0, or --class 0 2 3
    agnostic_nms=False  # class-agnostic NMS
    augment=False  # augmented inference
    visualize=False  # visualize features
    update=False  # update all models
    project='./runs/detect',  # save results to project/name
    name='exp'  # save results to project/name
    exist_ok=False  # existing project/name ok, do not increment
    line_thickness=3  # bounding box thickness (pixels)
    hide_labels=False  # hide labels
    hide_conf=False  # hide confidences
    half=False  # use FP16 half-precision inference
    dnn=False  # use OpenCV DNN for ONNX inference
    vid_stride=1  # video frame-rate stride

    device = select_device(device)
    model = DetectMultiBackend(weights, device=device, dnn=dnn, data=data, fp16=half)
    stride, names, pt = model.stride, model.names, model.pt

    imgsz = check_img_size(imgsz, s=stride)  # check image size

    bs = 1  # batch_size
    dataset = LoadImages(image_data, img_size=imgsz, stride=stride, auto=pt, vid_stride=vid_stride)

    vid_path, vid_writer = [None] * bs, [None] * bs

    model.warmup(imgsz=(1 if pt or model.triton else bs, 3, *imgsz))  # warmup
    seen, windows, dt = 0, [], (Profile(), Profile(), Profile())
    
    for path, im, im0s, vid_cap, s in dataset:
        with dt[0]:
            im = torch.from_numpy(im).to(model.device)
            im = im.half() if model.fp16 else im.float()  # uint8 to fp16/32
            im /= 255  # 0 - 255 to 0.0 - 1.0
            if len(im.shape) == 3:
                im = im[None]  # expand for batch dim

        # Inference
        with dt[1]:
            visualize = increment_path(save_dir / Path(path).stem, mkdir=True) if visualize else False
            pred = model(im, augment=augment, visualize=visualize)

        # NMS 같은 물체를 여러번 탐색하는데 그 중 하나만 선택하는 과정
        with dt[2]:
            pred = non_max_suppression(pred, conf_thres, iou_thres, classes, agnostic_nms, max_det=max_det)

    result = False
    for tensor in pred:
        for detection in tensor:
            if detection[4] > 0.4:
                result = True
                print(result)
                return result
    return result


if __name__ == "__main__":
    # certifyAurora('')
    app.run(debug=True, host='0.0.0.0', port=8080)