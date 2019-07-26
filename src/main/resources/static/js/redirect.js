const ACCESS_TOKEN = 'accessToken';

function getUrlParameter(param){
    const url = new URL(window.location.href);
    const res = url.searchParams.get(param);
    return res === null ? '' : res;
}

const token = getUrlParameter('token');
const error = getUrlParameter('error');

if(token){
    localStorage.setItem(ACCESS_TOKEN, token);
    window.location.href = '/oauth2/logInto';
}else{
    console.log("Powrot do strony startowej");
    window.location.href = '/';
}