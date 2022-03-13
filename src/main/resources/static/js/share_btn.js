const share_btn = document.getElementById('share_btn');
const qrc = document.querySelector('.co_qrc_con');

share_btn.addEventListener('click', function() {
        // 点击事件
        if(qrc.style.display=='none'){
            qrc.style.display='unset';
        }else{
            qrc.style.display='none';
        }
    }
);
