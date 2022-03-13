var qrcode = new QRCode('qrcode', {
    text: 'http://localhost:8080/index',
    width: 104,
    height: 104,
    colorDark : 'black',
    colorLight : '#ffffff',
    correctLevel : QRCode.CorrectLevel.H
});

// 使用 API
qrcode.clear();
qrcode.makeCode('http://localhost:8080/index');
