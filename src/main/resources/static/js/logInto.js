const ACCESS_TOKEN = 'accessToken';
const API_BASE_URL = '/oauth2';

if(localStorage.getItem(ACCESS_TOKEN) === null){
    alert("Access token not set");
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
                return json;
            }).catch(error => {
                    console.log(error);
                })
        );
};

function getCustomizedExercises(){
    const age = document.getElementById("inputAge").value;
    const inputRelShape = document.getElementById("inputRelShape").value;
    const inputWeight = document.getElementById("inputWeight").value;
    const inputHeight = document.getElementById("inputHeight").value;

    const inputs = {
        age: age,
        weight: inputWeight,
        height: inputHeight,
        relativeShape: inputRelShape
    };
    const loginRequest = Object.assign({}, inputs);

    return request({
        url: API_BASE_URL + '/getCustomizedExercises',
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

$(document).ready(function () {

    $("#arrangeForm").submit(function (event) {

        event.preventDefault();

        fire_ajax_submit();

        var ajaxPostInfoDiv = document.getElementById("ajaxPostInfoDiv");
        ajaxPostInfoDiv.innerHTML = "<strong>Dodałeś nowy plan ćwiczenia</strong>";
        ajaxPostInfoDiv.style.backgroundColor = "ForestGreen";
        $("#ajaxPostInfoDiv").fadeIn();
        setTimeout(function(){
            $("#ajaxPostInfoDiv").fadeOut();
        }, 3000);

    });

});

function fire_ajax_submit() {

    const userId = document.getElementById("userId").value;
    console.log("USERID = " + userId);
    var search = {};
    search["name"] = $("#name").val();
    search["rounds"] = $("#rounds").val();
    search["repetitionsInOneRound"] = $("#repetitionsInOneRound").val();
    search["date"] = $("#date").val();
    $("#btn-search").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/oauth2/arrangeTrainingPart/" + userId,
        data: JSON.stringify(search),
        dataType: 'json',
        cache: false,
        success: function () {
            var json = "<h4>Ajax Response</h4><pre>";
            $('#feedback').html('<h4>Ajax Response</h4><pre>');
        }
    });
}

function logout(){
    if(localStorage.getItem(ACCESS_TOKEN) !== null){
        localStorage.removeItem(ACCESS_TOKEN);
        window.location.href = '/';
    }
}