package application;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Utils;

/**
 * This controller is the main frame of the application. All the logic is
 * implemented here.
 * 
 * What controller does: - Start/stops camera usage - Classifier controls -
 * Video stream - Face detection/tracking
 * 
 * コントローラクラス
 */
public class Controller {

	@FXML
	private Button button;
	@FXML
	private ImageView currentFrame;

	/**
	 * Checkbox to enable/disable classifier
	 */
	@FXML
	private CheckBox haarClassifier;
	@FXML
	private CheckBox lbpClassifier;

	// OpenCV object that manipulates video capture
	private VideoCapture capture;

	// Scheduled timer to aquire frame of video capture
	private ScheduledExecutorService timer;

	// Check if camera is running
	private boolean cameraActive = false;

	// Camera id
	private static int cameraID;

	// face cascade classifier
	private CascadeClassifier faceDetector;
	private int absoluteFaceSize;

	// Check if either checkboxes are checked
	private boolean checkIfChecked;

	/**
	 * Initialization of controller at start time
	 */
	protected void initialize() {
		this.capture = new VideoCapture();
		this.faceDetector = new CascadeClassifier();
		this.absoluteFaceSize = 0;

		// Set fixed width of frame
		currentFrame.setFitWidth(600);
		// Preserve image ratio
		currentFrame.setPreserveRatio(true);

		// Camera ID for camera usage
		cameraID = 0;

		// See if checkboxes are checked, initially not.
		checkIfChecked = false;
	}

	/**
	 * Executed after the FXML button is pushed
	 * 
	 * @param event
	 *            button push event
	 */
	@FXML
	protected void startCamera(ActionEvent event) {

		// Check if either checkboxes are checked, if not program will crash severely.
		if (!checkIfChecked) {
			System.err.println("Select either Haar or LBP Classifier.");
			this.cameraActive = true;
		}

		if (!this.cameraActive) {

			// Start video capture
			this.capture.open(cameraID);

			// Check if video stream is opened
			if (this.capture.isOpened()) {

				// Passes if video stream is opened
				this.cameraActive = true;

				this.button.setText("ストップ");

				// Capture frame every 33 ms (30 fps)
				Runnable frameGrabber = new Runnable() {

					@Override
					public void run() {
						// effectively grab and process a single frame
						Mat frame = grabFrame();
						// convert and show the frame
						Image imageToShow = Utils.mat2Image(frame);
						updateImageView(currentFrame, imageToShow);
					}

				};

				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

			}

		} else {

			// Camera is not running at this point
			this.cameraActive = false;

			this.button.setText("もう一回スタート");

			// Stop timer
			this.stopAcquisition();

		}

	}

	/**
	 * Get a frame from the commenced video stream (if any)
	 * 
	 * @return frame from {@link Image}
	 */
	private Mat grabFrame() {

		Mat frame = new Mat();

		// check if the capture is open
		if (this.capture.isOpened()) {
			try {
				// read the current frame
				this.capture.read(frame);

				// Process if frame isn't empty
				if (!frame.empty()) {
					// Call face detection
					this.detectFace(frame);
				}
			} catch (Exception e) {
				System.err.println("Exception during the frame elaboration: " + e);
			}
		}

		return frame;
	}

	/**
	 * Method to face detection and tracking
	 * 
	 * @param frame
	 *            single frame which is used to detect face
	 */
	private void detectFace(Mat frame) {

		MatOfRect faces = new MatOfRect();
		Mat grayFrame = new Mat();

		// Convert frame to gray scale
		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGRA2GRAY);
		// Equalize the frame histogram for more efficient detection
		Imgproc.equalizeHist(grayFrame, grayFrame);

		// Calculate minimum face size
		if (this.absoluteFaceSize == 0) {
			// Number of rows indicate the height
			int height = grayFrame.rows();
			if (Math.round(height * 0.2f) > 0) {
				this.absoluteFaceSize = Math.round(height * 0.2f);
			}
		}

		// Detect faces
		this.faceDetector.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

		// Draw rectangle to frame where it detects a face
		Rect[] arrayOfFaces = faces.toArray();
		for (int i = 0; i < arrayOfFaces.length; i++) {
			Imgproc.rectangle(frame, arrayOfFaces[i].tl(), arrayOfFaces[i].br(), new Scalar(0, 255, 0, 255), 3);
		}

	}

	/**
	 * The action triggered by selecting the Haar Classifier checkbox. It loads the
	 * trained set to be used for frontal face detection.
	 */
	@FXML
	protected void haarSelected(Event event) {

		// This point, haar checkbox is selected, therefore program can continue on in
		// startCamera()
		if (!this.checkIfChecked)
			this.checkIfChecked = true;

		// check whether the lpb checkbox is selected and deselect it
		if (this.lbpClassifier.isSelected())
			this.lbpClassifier.setSelected(false);

		this.checkboxSelection("resources/haarcascades/haarcascade_frontalface_alt.xml");
	}

	/**
	 * The action triggered by selecting the LBP Classifier checkbox. It loads the
	 * trained set to be used for frontal face detection.
	 */
	@FXML
	protected void lbpSelected(Event event) {

		// This point, haar checkbox is selected, therefore program can continue on in
		// startCamera()
		if (!this.checkIfChecked)
			this.checkIfChecked = true;

		// check whether the haar checkbox is selected and deselect it
		if (this.haarClassifier.isSelected())
			this.haarClassifier.setSelected(false);

		this.checkboxSelection("resources/lbpcascades/lbpcascade_frontalface.xml");
	}

	/**
	 * Method for loading a classifier trained set from disk
	 * 
	 * @param classifierPath
	 *            the path on disk where a classifier trained set is located
	 */
	private void checkboxSelection(String classifierPath) {
		// load the classifier(s)
		this.faceDetector.load(classifierPath);

		// now the video capture can start
		this.button.setDisable(false);
	}

	/**
	 * Update {@link ImageView} in JavaFX main thread
	 * 
	 * @param view
	 *            show {@link ImageView}
	 * @param image
	 *            show {@link Image}
	 */
	private void updateImageView(ImageView view, Image image) {

		Utils.onFXThread(view.imageProperty(), image);

	}

	/**
	 * Stop the acquisition which would release all resources from camera
	 */
	private void stopAcquisition() {

		if (this.timer != null && !this.timer.isShutdown()) {
			try {

				// Stop timer
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);

			} catch (InterruptedException e) {
				System.err.println("Error: Unable to stop frame: " + e);
			}
		}
	}

	public void setClosed() {
		this.stopAcquisition();
	}
	
	public void quit() {
		System.exit(0);
	}

}
