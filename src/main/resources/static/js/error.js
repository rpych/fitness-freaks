const ACCESS_TOKEN = 'accessToken';

setTimeout(function(){
    if(localStorage.getItem(ACCESS_TOKEN) !== null){
        localStorage.removeItem(ACCESS_TOKEN);
    }
    window.location.href = '/';
}, 8000);