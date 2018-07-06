**face_detection** - Facial detection application using OpenCV. OpenCVを使ってる顔検出アプリ。

**Java:**

<img src="https://github.com/oasysokubo/face_detection/blob/master/resources/img_readme/trump_gif.gif" width="450">


**What is Haar and LBP Classifiers?**
-------------------------------
- LBP, Local Binary Patterns, cascade is a much faster algorithm than haar, but less accurate (10-20% less than Haar) because
LBP uses integers for the calculations, which is better for embedded systems.
- Haar cascade uses floating point arithmetic, which takes longer due to more complex calculations.


**Requirements**
------------------
- Install OpenCV for Java: [Installation steps for all platforms](https://github.com/opencv-java/opencv-java-tutorials/blob/master/docs/source/01-installing-opencv-for-java.rst)


**Built With:**
---------------
- [OpenCV](https://opencv.org) - Computer Vision Library
