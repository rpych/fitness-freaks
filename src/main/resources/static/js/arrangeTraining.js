const ACCESS_TOKEN = 'accessToken';
const API_BASE_URL = '/oauth2';

if(localStorage.getItem(ACCESS_TOKEN) === null){
    console.log("ACCESS_TOKEN not valid in logInto.js");
    window.location.href = '/';
}

const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    });

    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
        .then(response =>
            response.json().then(json => {
                if(!response.ok) {
                    return Promise.reject(json);
                }
                console.log('Odpowiedz ' + json);
                return json;
            }).catch(error => {
                console.log(error);
            })
        );
};


function arrangeTraining(){
    return request({
        url: API_BASE_URL + '/arrangeTraining',
        method: 'GET',
    });
}