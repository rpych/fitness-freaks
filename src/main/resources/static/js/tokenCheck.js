const ACCESS_TOKEN = 'accessToken';

if(localStorage.getItem(ACCESS_TOKEN) === null){
    alert("Access token not set");
    window.location.href = '/';
}