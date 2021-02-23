function submit() {
    let el = document.getElementById("currency");
    let statusDiv = document.getElementById('status');
    statusDiv.style.display = 'none';
    statusDiv.innerText = "";
    let resultDiv = document.getElementById('result');
    resultDiv.style.display = 'none';
    let currency = el.options[el.selectedIndex].value;

    let xhr = new XMLHttpRequest();
    xhr.responseType = 'blob';
    xhr.open("GET", "api/start/" + currency);
    xhr.send();

    xhr.onload = function() {
        if (xhr.status != 200) {
            resultDiv.style.display = 'none';
            let fr = new FileReader();
            fr.onload = function(e){
                let json = JSON.parse(e.target.result);
                statusDiv.style.display = 'block';
                statusDiv.innerText = `Ошибка ${json.status}: ${json.message}`;
            }
            fr.readAsText(xhr.response);
        } else {
            statusDiv.style.display = 'none';
            resultDiv.style.display = 'block';

            let resultText = document.getElementById('result-text');
            let resultImg = document.getElementById('result-img');
            resultImg.src = "";
            resultText.innerText = "";

            if(xhr.response.size == 0) {
                resultText.innerText = "Курс не изменился";
                return;
            }

            let urlCreator = window.URL || window.webkitURL;
            let imageUrl = urlCreator.createObjectURL(xhr.response);
            resultImg.src = imageUrl;

        }
    };

    xhr.onprogress = function(event) {
        if (event.lengthComputable) {
            statusDiv.style.display = 'block';
            statusDiv.innerText = "Загрузка...";
        } else {
            statusDiv.style.display = 'none';
            statusDiv.innerText = "" ;
        }
    };

    xhr.onerror = function() {
        statusDiv.style.display = 'block';
        statusDiv.innerText = `Ошибка ${xhr.status}: ${xhr.statusText}`;
    };
}