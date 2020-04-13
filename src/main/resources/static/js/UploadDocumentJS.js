$(document).ready(function() {
    $("#Loader").hide();
    InlineEditor
        .create(document.querySelector('#editor'), {
            toolbar: ['Heading', '|', 'bold', 'italic', 'bulletedList', 'numberedList', 'blockQuote', 'Link', 'Undo', 'Redo']
        })
        .then(editor => {
            $("#UploadDocumentButton").hover(function() {
                document.getElementById("MainTextArea").innerHTML = editor.getData();
                console.log(editor.getData());
            });
        })
        .catch(error => {
            console.error(error);
        });

    var paidAccess = false;
    $("#MainUserDocumentFileUploadButton").change(function() {
        var file = document.getElementById("MainUserDocumentFileUploadButton");
        if (file.files.length > 0) {
            $("#DocumentUploadedNotification").show();
        } else {
            $("#DocumentUploadedNotification").hide();
        }
    });

    $("#UploadDocumentButton").click(function() {
        $("#Loader").show();
        var checkFields = checkAllFields();
        if (checkFields) {
            //AJAX Request
            var economicAccessPrice = 0;
            if (paidAccess) {
                economicAccessPrice = document.getElementById("PriceInput").value;
            }
            var date = (("" + new Date().toISOString()).replace("T", " ")).replace("Z", "");
            console.log("MainText: " + document.getElementById("MainTextArea").innerText);
            var settings = {
                "async": true,
                "crossDomain": true,
                "url": "http://localhost:8080/document/upload",
                "method": "POST",
                "timeout": 0,
                "headers": {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                "processData": false,
                "data": JSON.stringify({
                    "memberId": parseInt(localStorage.getItem("memberId")),
                    "economicAccess": parseFloat(economicAccessPrice),
                    "creationDate": date,
                    "documentName": document.getElementById("MainDocumentNameInputText").value,
                    "documentDescription": document.getElementById("MainTextArea").innerText
                }),
            };

            $.ajax(settings).done(function(data, statusText, xhr) {
                if (xhr.status == 201) {
                    var form = new FormData();
                    form.append("file", document.getElementById("MainUserDocumentFileUploadButton").files[0]);

                    var settings = {
                        "async": true,
                        "crossDomain": true,
                        "url": "http://localhost:8080/document/upload/" + localStorage.getItem("memberId") + "/" + data,
                        "method": "POST",
                        "timeout": 0,
                        "processData": false,
                        "mimeType": "multipart/form-data",
                        "contentType": false,
                        "data": form
                    };
                    $.ajax(settings).done(function(data, statusText, xhr) {
                        if (xhr.status == 201) {
                            $("#Loader").hide();
                            alert("File Uploaded Successfully");
                        }
                    });
                }

            });
        } else {
            $.notify("Please fill All the required Details", "warn");
        }
    });

    $("#PaidAccess").click(function() {
        $("#Price").show();
        $("#ShowFree").hide();
        paidAccess = true;
    });

    $("#FreeAccess").click(function() {
        $("#Price").hide();
        $("#ShowFree").show();
        paidAccess = false;
    });

    function checkAllFields() {
        var documentName = checkDocumentName();
        var documentDescription = checkDocumentDescription();
        var document = checkDocumentUpload();
        if (paidAccess) {
            var checkPrice = checkForPrice();
            if (!checkPrice) {
                $.notify("Please fill Price of Document", "warn");
                return false;
            }
        }
        if (documentName && documentDescription && document) {
            return true;
        }
        return false;
    }

    function checkDocumentName() {
        var documentName = document.getElementById("MainDocumentNameInputText").value;
        if (documentName.length > 20) {
            $.notify("Document name should not be greater than 20 characters", "warn");
            return false;
        }
        if (documentName.length > 0) {
            return true;
        }
        $.notify("Please fill Name of Document", "warn");
        return false;
    }

    function checkDocumentDescription() {
        var documentDescription = document.getElementById("MainTextArea").innerText;
        if (documentDescription.length > 0) {
            return true;
        }
        $.notify("Please fill Description of Document", "warn");
        return false;
    }

    function checkDocumentUpload() {
        var file = document.getElementById("MainUserDocumentFileUploadButton");
        if (file.files.length > 0) {
            return true;
        }
        $.notify("Please upload Document", "warn");
        return false;
    }

    function checkForPrice() {
        var price = document.getElementById("PriceInput").value;
        if (price.length > 0) {
            return true;
        }
        if (price > 99999) {
            $.notify("Price cannot be greater than 99999", "warn");
        }
        return false;
    }

    $("#circuralActivity").click(function() {
        localStorage.setItem("memberId", "");
        localStorage.setItem("planId", "");
        sessionStorage.setItem("URLToRequest", "");
        window.location.href = "http://localhost:8080/html/Login.html";
    });

});