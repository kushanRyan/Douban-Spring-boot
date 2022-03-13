const select_song = document.querySelector('.co_songs_lists');
const realted_sub = document.querySelector('.co_subject_lists');

const songs_lists = document.querySelector('.co_select-song');
const subject_lists = document.querySelector('.co_realted_sub');

select_song.addEventListener('click', function () {
    subject_lists.style.display="none";
    songs_lists.style.display="unset";
    select_song.style.opacity="1";
    realted_sub.style.opacity="0.4";
})

realted_sub.addEventListener('click', function () {
    subject_lists.style.display="unset";
    songs_lists.style.display="none";
    select_song.style.opacity="0.4";
    realted_sub.style.opacity="1";
})

