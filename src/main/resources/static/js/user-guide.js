const changer = document.querySelector('.changer_box');

fetch(
    'http://127.0.0.1:8080/singer/random'
)
    .then(function (response) {
        return response.json();
    })
    .then(function (myJson) {
        changer.addEventListener('click', function () {
            for(let i=1;i<myJson.length;i++) {
                let aData=myJson[i];
                console.log(aData.avatar);
                document.getElementById(i+'a').src = aData.avatar;
                document.getElementById(i+'a').text = aData.name;
            }
        })

    });



