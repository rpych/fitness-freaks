
const userId = document.getElementById("userId").value;
const ACCESS_TOKEN = 'accessToken';
const API_BASE_URL = '/oauth2';
const CURRENT_VIEW_HREF = '/oauth2/arrangeTraining/' + userId;

if(localStorage.getItem(ACCESS_TOKEN) === null){
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
                console.log(json);
                setFeedback();
                setTimeout(function(){
                    window.location.href = CURRENT_VIEW_HREF;
                }, 2000);
                return json;
            })
        );
};

function setFeedback(){
    var feedback = document.getElementById("feedbackDiv");
    feedback.innerHTML = "<strong>Ćwiczenie zostało usunięte</strong>";
    feedback.style.backgroundColor = "red";
}

function deleteExercise(){
    var elementToDelete = document.getElementById("inputExercise");
    var option = elementToDelete.options[elementToDelete.selectedIndex];
    var indexToDelete = option.value;


    return request({
        url: API_BASE_URL + '/deleteElement/' + userId + '/' + indexToDelete,
        method: 'DELETE'
    });
}