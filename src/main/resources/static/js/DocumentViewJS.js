$(document).ready(function() {
    if (localStorage.getItem("planId") != 1) {
        var displayHeader = "                <div class=\"subbordinateActivities\">\r\n" +
            "                    <a href=\"http://localhost:8080/html/UploadDocument.html\" id=\"uploadDocument\" style=\"text-decoration:none;color:rgb(154, 231, 154);\">Upload Document</a>\r\n" +
            "                </div>";
        document.getElementById("subbordinateActivity").innerHTML += displayHeader;
    }
    $("#SendMessageIconId").click(function() {
        var feedbackText = document.getElementById("MainTextArea").innerText;
        if (feedbackText.length == 0) {
            $.notify("Please fill Feedback of Document", "warn");
        }
        if (feedbackText.length > 125) {
            $.notify("Feedback text should not be greate than 125 characters", "warn");
        }
    });

    var access = false;
    var economicAccess = 0.0;
    var trun = 0;
    var url = "";
    var settings = {
        "async": true,
        "crossDomain": true,
        "url": sessionStorage.getItem("URLToRequest"),
        "method": "GET",
        "timeout": 0,
        "headers": {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        "processData": false
    };
    $.ajax(settings).done(function(data, statusText, xhr) {
        console.log(xhr);
        console.log(data);
        if (xhr.status == 200) {
            access = data.currentMemberAccess;
            economicAccess = data.economicAccess;
            url = data.documentURL;
            document.getElementById("ContentViewer").innerHTML = data.documentDescription;
            document.getElementById("DocumentNameAlign").innerHTML = data.documentName;
            document.getElementById("CreationDateText").innerHTML = data.creationDate;
            document.getElementById("MimeText").innerHTML = data.fileType;
            document.getElementById("EconomicAccessText").innerHTML = data.economicAccess;
            if (data.currentMemberAccess) {
                document.getElementById("thisActivity").innerHTML = "<div style=\"text-decoration:none; color: rgb(217, 235, 236);\" id=\"URL\">View Document </div> ";
                turn = 1;
            } else if (!access && economicAccess == 0.0) {
                document.getElementById("thisActivity").innerHTML = "<div style=\"text-decoration:none; color: rgb(217, 235, 236);\" id=\"URL\">Subscribe Member </div> ";
                turn = 2;
            } else if (!access && economicAccess != 0.0 && localStorage.getItem("planId") != 1) {
                document.getElementById("thisActivity").innerHTML = "<div style=\"text-decoration:none; color: rgb(217, 235, 236);\" id=\"URL\">Buy Document </div> ";
                turn = 3;
            } else {
                document.getElementById("thisActivity").innerHTML = "<div style=\"text-decoration:none; color: rgb(217, 235, 236);\" id=\"URL\">No Access </div> ";
                turn = 4;
            }
            console.log("TURN: " + turn);
        }
    });
    $(document).on('click', '#URL', function() {
        if (turn != 0) {
            if (turn == 1) {
                console.log(url);
                window.location.href = url;
            }
            if (turn == 2) {
                var settings = {
                    "async": true,
                    "crossDomain": true,
                    "url": url,
                    "method": "POST",
                    "timeout": 0,
                    "headers": {
                        "Content-Type": "application/json",
                        "Accept": "application/json"
                    },
                    "processData": false
                };
                $.ajax(settings).done(function(data, statusText, xhr) {
                    location.reload();
                    console.log(xhr.status + " : " + data);
                    if (xhr.status == 200) {
                        alert(data);
                        location.reload();
                    }
                });
            }
            if (turn == 3) {
                console.log("Here in Turn 3");
                console.log("URL:" + url);
                var settings = {
                    "async": true,
                    "crossDomain": true,
                    "url": url,
                    "method": "POST",
                    "timeout": 0,
                    "headers": {
                        "Content-Type": "application/json",
                        "Accept": "application/json"
                    },
                    "processData": false
                };
                $.ajax(settings).done(function(data, statusText, xhr) {
                    location.reload();
                    console.log("Here" + xhr.status + " : " + data);
                    if (xhr.status == 200) {
                        alert(data);
                        location.reload();
                    }
                });
            }
        }
    });


    $("#circuralActivity").click(function() {
        localStorage.setItem("memberId", "");
        localStorage.setItem("planId", "");
        sessionStorage.setItem("URLToRequest", "");
        window.location.href = "http://localhost:8080/html/Login.html";
    });

});