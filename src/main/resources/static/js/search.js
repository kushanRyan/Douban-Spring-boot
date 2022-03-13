const inputer = document.querySelector('.search_bar');
const lists = document.querySelector('.search_artists');
let theHtml = '';
let tempHtml='';

function processer(myJson){
    var songs = myJson["songs"];
    debugger
    for(let i = 0 ;i<songs.length;i++){

        let aData = songs[i];
        if( i<=5 && aData["name"]!=''){
            tempHtml=tempHtml+'<div class="search_aArtist"><img class="search_artist_lists_artist" th:src="${\'../pics/artist_test.png\'}"><div class="search_artist_lists_text">${aData.name}</div></div>'
        }
    }
    return tempHtml;
}

inputer.addEventListener('input', function() {
        let url ='http://127.0.0.1:8080/searchContent?keyword='+inputer.value;
        tempHtml='';
        fetch(
            url
        )
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {
            theHtml=processer(myJson);
            lists.innerHTML= theHtml;
        });

    }
);

