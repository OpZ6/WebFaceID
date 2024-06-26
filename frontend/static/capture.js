// The width and height of the captured photo. We will set the
// width to the value defined here, but the height will be
// calculated based on the aspect ratio of the input stream.

var width = 320; // We will scale the photo width to this
var height = 0; // This will be computed based on the input stream

// |streaming| indicates whether or not we're currently streaming
// video from the camera. Obviously, we start at false.

var streaming = false;

// The various HTML elements we need to configure or control. These
// will be set by the startup() function.

var video = null;
var canvas = null;
var photo = null;
var startbutton = null;

function startup() {
  video = document.getElementById("video");
  canvas = document.getElementById("canvas");
  photo = document.getElementById("photo");
  startbutton = document.getElementById("startbutton");
  uploadbutton = document.getElementById("uploadbutton");

  navigator.mediaDevices
    .getUserMedia({ video: true, audio: false })
    .then(function (stream) {
      video.srcObject = stream;
      video.play();
    })
    .catch(function (err) {
      console.log("An error occurred: " + err);
    });

  video.addEventListener(
    "canplay",
    function (ev) {
      if (!streaming) {
        height = video.videoHeight / (video.videoWidth / width);

        // Firefox currently has a bug where the height can't be read from
        // the video, so we will make assumptions if this happens.

        if (isNaN(height)) {
          height = width / (4 / 3);
        }

        video.setAttribute("width", width);
        video.setAttribute("height", height);
        canvas.setAttribute("width", width);
        canvas.setAttribute("height", height);
        streaming = true;
      }
    },
    false
  );

  startbutton.addEventListener(
    "click",
    function (ev) {
      takepicture();
      ev.preventDefault();
    },
    false
  );

  clearphoto();

  uploadbutton.addEventListener(
    "click",
    function (ev) {
      takepicture();
      uploadImage(photo.src);
      ev.preventDefault();
    },
    false
  );
}

// Fill the photo with an indication that none has been
// captured.

function clearphoto() {
  var context = canvas.getContext("2d");
  context.fillStyle = "#AAA";
  context.fillRect(0, 0, canvas.width, canvas.height);

  var data = canvas.toDataURL("image/png");
  photo.setAttribute("src", data);
}

// Capture a photo by fetching the current contents of the video
// and drawing it into a canvas, then converting that to a PNG
// format data URL. By drawing it on an offscreen canvas and then
// drawing that to the screen, we can change its size and/or apply
// other changes before drawing it.

function takepicture() {
  var context = canvas.getContext("2d");
  if (width && height) {
    canvas.width = width;
    canvas.height = height;
    context.drawImage(video, 0, 0, width, height);

    var data = canvas.toDataURL("image/png");
    photo.setAttribute("src", data);
  } else {
    clearphoto();
  }
}

// const backend_host = "https://backendltcv1.azurewebsites.net";
// const backend_host = "http://localhost:8080";
backend_host = "";
function uploadImage(data) {
  fetch(backend_host + "/images", {
    method: "POST",
    body: data,
  })
    .then((result) => {
      console.log("Success:", result);
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}


function validateImage() {
  var context = canvas.getContext('2d');
  canvas.width = width;
  canvas.height = height;
  context.drawImage(video, 0, 0, width, height);

  var data = canvas.toDataURL('image/png');
  photo.setAttribute('src', data);

  fetch(backend_host + '/validate', {
    method: 'POST',
    body: data
  })
    .then(response => response.json())
    .then(data => {
      result = document.getElementById('result');
      result.innerHTML = "";
      data = JSON.parse(data.body);
      for (var i = 0; i < data.predictions.length; i++) {
        console.log("Tag Name: " + data.predictions[i].tagName + " Probability: " + data.predictions[i].probability);
        result.innerHTML += "Tag Name: " + data.predictions[i].tagName + " Probability: " + data.predictions[i].probability + "<br>";
      }
    })
    .catch(error => {
      console.error('Error:', error);
    });
}

function submitForm(event) {
  event.preventDefault();
  var context = canvas.getContext('2d');
  canvas.width = width;
  canvas.height = height;
  context.drawImage(video, 0, 0, width, height);

  var data = canvas.toDataURL('image/png');
  photo.setAttribute('src', data);

  var name = document.getElementById("fullname").value;
  var email = document.getElementById("email").value;

  fetch(backend_host + '/form', {
    method: 'POST',
    body: JSON.stringify({
      name: name,
      email: email,
      image: data
    })
  })
    .then(result => {
      console.log('Success:', result);
    })
    .catch(error => {
      console.error('Error:', error);
    });
}


function getImage(event) {
  event.preventDefault();

  var id = document.getElementById("UserId").value;
  var requestUrl = backend_host + '/image/' + id;

  fetch(requestUrl, {
    method: 'GET',
  })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok ' + response.statusText);
      }
      return response.json(); // 使用json()来获取JSON对象
    })
    .then(data => {
      // 提取Base64图像数据
      var base64Image = data.image;
      // 设置<img>元素的src为Base64字符串
      var imageElement = document.getElementById('serverImage');
      imageElement.src = 'data:image/png;base64,' + base64Image;
      imageElement.style.display = 'block'; // 确保图片显示
    })
    .catch(error => {
      console.error('Error:', error);
    });
}

// Set up our event listener to run the startup process
// once loading is complete.
window.addEventListener("load", startup, false);
