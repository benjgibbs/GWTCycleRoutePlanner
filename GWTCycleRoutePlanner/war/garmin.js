

var display = new Garmin.DeviceDisplay("garminDisplay", {
     autoFindDevices: true, //start searching for devices
     showStatusElement: true, //basic feedback provided
     showReadDataElement: false, //don't offer to read data
     //add other options per the documentation
});

document.write("Hello")