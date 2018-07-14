//
//  DetectFace.hpp
//  Face Detection
//
//  Created by Oasys Okubo on 7/6/18.
//  Copyright © 2018 Oasys Okubo. All rights reserved.
//

#ifndef DetectFace_hpp
#define DetectFace_hpp

#include "opencv2/opencv.hpp"

#include <stdio.h>

using namespace cv;
using namespace std;

class DetectFace {
public:
    
private:
    cv::Mat detect_face(Mat);
};

#endif /* DetectFace_hpp */
