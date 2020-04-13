var data = "";
var selected = -1;
var mails = ["mail.com", "ahoo.com", "b.in", "ediffmail.com"];
var firstMailEntry = false;
$("#EmailInputText").keyup(function() {
    var inAt = false;
    var getIn = false;
    var hurrayDotPresent = false;
    var contentsAfterAt = "";
    var mailContent = document.getElementById("EmailInputText").value;
    if (mailContent.charAt(0) != '@') {
        for (var i = 0; i < mailContent.length; i++) {
            if (getIn) {
                contentsAfterAt += mailContent.charAt(i);
            }
            if (!inAt) {
                if (mailContent.charAt(i) == '@') {
                    inAt = true;
                    getIn = true;
                }
            }
            if (inAt) {
                if (mailContent.charAt(i) == '.') {
                    hurrayDotPresent = true;
                    break;
                }

            }

        }
    } else {
        $.notify("Please do not @ at starting", "warn");
        document.getElementById("EmailInputText").value = "";
    }
    if (firstMailEntry == true) {
        if (contentsAfterAt == "") {
            hurrayDotPresent = false;
        } else {
            hurrayDotPresent = true;
        }
    }
    var contentsOfMail = document.getElementById("EmailInputText").value;
    var atPresent = false;
    var dotPresent = false;
    for (var i = 0; i < contentsOfMail.length; i++) {
        if (contentsOfMail.charAt(i) == '@') {
            atPresent = true;
        }
        if (atPresent) {
            if (contentsOfMail.charAt(i) == '.') {
                dotPresent = true;
                break;
            }
        }
    }
    if (!hurrayDotPresent) {
        selected = -1;
    }
    if (getIn && selected == -1 && !dotPresent) {
        if (event.keyCode == 71) {
            selected = 0;
            var mail = document.getElementById("EmailInputText").value;
            document.getElementById("EmailInputText").value = mail + mails[0];
            firstMailEntry = true;
        } else if (event.keyCode == 89) {
            selected = 1;
            var mail = document.getElementById("EmailInputText").value;
            document.getElementById("EmailInputText").value = mail + mails[1];
            firstMailEntry = true;
        } else if (event.keyCode == 68) {
            selected = 2;
            var mail = document.getElementById("EmailInputText").value;
            document.getElementById("EmailInputText").value = mail + mails[2];
            firstMailEntry = true;
        } else if (event.keyCode == 82) {
            selected = 3;
            var mail = document.getElementById("EmailInputText").value;
            document.getElementById("EmailInputText").value = mail + mails[3];
            firstMailEntry = true;
        }
    }
});


$("#NextForPasswordOnFirstQuery").on("mouseover mouseenter hover click", function() {

    if (selected == -1) {
        var mailContents = document.getElementById("EmailInputText").value;
        var atPresent = false;
        var dotPresent = false;
        var anonymousExtension = "";
        for (var i = 0; i < mailContents.length; i++) {
            if (atPresent) {
                if (mailContents.charAt(i) == '.') {
                    break;
                }
                anonymousExtension += mailContents.charAt(i);
            }
            if (mailContents.charAt(i) == '@') {
                atPresent = true;
            }

        }
        if (anonymousExtension == "gmail") {
            selected = 0;
        } else if (anonymousExtension == "yahoo") {
            selected = 1;
        } else if (anonymousExtension == "ivgroup") {
            selected = 2;
        } else if (anonymousExtension == "rediffmail") {
            selected = 3;
        }
    }

    var newMail = "";
    var mailContent = document.getElementById("EmailInputText").value;
    var extensionMail = "";
    var atHere = false;
    var presentMail = "";
    for (var i = 0; i < mailContent.length; i++) {
        if (mailContent.charAt(i) == '@') {
            atHere = true;
        }
        if (atHere) {
            if (mailContent.charAt(i) == '.') {
                break;
            }
            extensionMail += mailContent.charAt(i);
        }
    }
    var fullCorrect = true;
    if (selected == 0) {
        var presentMail = "@g" + mails[selected];
    } else if (selected == 1) {
        var presentMail = "@y" + mails[selected];
    } else if (selected == 2) {
        var presentMail = "@i" + mails[selected];
    } else if (selected == 3) {
        var presentMail = "@r" + mails[selected];
    }
    for (var i = 0; i < extensionMail.length; i++) {
        if (presentMail.charAt(i) != extensionMail.charAt(i)) {
            fullCorrect = false;
            break;
        }
        if (extensionMail.charAt(i) == '.') {
            break;
        }
    }
    if (fullCorrect) {
        for (var i = 0; i < mailContent.length; i++) {
            newMail += mailContent.charAt(i);
            if (selected != -1) {
                if (mailContent.charAt(i) == '@') {
                    if (selected == 0) {
                        newMail += ("g" + mails[selected]);
                    } else if (selected == 1) {
                        newMail += ("y" + mails[selected]);
                    } else if (selected == 2) {
                        newMail += ("i" + mails[selected]);
                    } else if (selected == 3) {
                        newMail += ("r" + mails[selected]);
                    }
                    break;
                }
            }
        }
        document.getElementById("EmailInputText").value = newMail;
    } else {
        var newStart = 0;
        var canExtend = false;
        for (var i = 0; i < mailContent.length; i++) {
            newMail += mailContent.charAt(i);
            if (!canExtend) {
                if (mailContent.charAt(i + 1) == '@') {
                    newStart = i + 1;
                    newMail += extensionMail;
                    newMail += '.';
                    i += (extensionMail.length + 1);
                    canExtend = true;
                }
            }
        }
        document.getElementById("EmailInputText").value = newMail;
    }

});

function finalValidateEmail() //validates email field
{
    var atTheRate = false;
    var dot = false;
    var email = document.getElementById("EmailInputText").value;
    var dotAfterCharCount = 0;
    for (var i = 0; i < email.length; i++) {
        if (dot) {
            dotAfterCharCount++;
        }
        if (email.charAt(i) == '@') //checks for @ in email field
        {
            atTheRate = true; //@ is present
        }
        if (atTheRate) {
            if (email.charAt(i) == '.') //checks for . in email field
            {
                dot = true; //. is present
            }
        }
    }
    if (atTheRate && dot && dotAfterCharCount > 1) //checks if both(@ and .) are present
    {
        return true; //present
    }
    return false; //not present
}

$("#NextForPasswordOnFirstQuery").click(function() {

});


var clickOnFirstCheck = false;
$(document).ready(function() {
    $("#PasswordContent").hide();
    $("#NextForPasswordOnFirstQuery").click(function() {

        var checkMail = finalValidateEmail();
        if (checkMail == false) {
            $("#NextForPasswordOnFirstQuery").show();
            $("#PasswordContent").hide();
            clickOnFirstCheck = false;
        } else {
            var settings = {
                "async": true,
                "crossDomain": true,
                "url": "http://localhost:8080/member/login/" + document.getElementById("EmailInputText").value,
                "method": "GET",
                "timeout": 0,
                "headers": {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                "processData": false
            };

            $.ajax(settings).done(function(data, statusText, xhr) {
                if (xhr.status == 200) {
                    if ($("#EmailInputText").val().length > 0) {
                        $("#NextForPasswordOnFirstQuery").hide();
                        $("#PasswordContent").show();
                        clickOnFirstCheck = true;
                    }
                }
                if (xhr.status == 204) {
                    $("#EmailInputText").notify(
                        "Please Enter Valid EmailId", { position: "right" }
                    );
                }
            });
        }
    });

    $("#NextForPasswordOnSecondQuery").click(function() {
        var settings = {
            "async": true,
            "crossDomain": true,
            "url": "http://localhost:8080/member/login?email=" + document.getElementById("EmailInputText").value + "&password=" + document.getElementById("PasswordInputText").value,
            "method": "GET",
            "timeout": 0,
            "headers": {
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            "processData": false
        };

        $.ajax(settings).done(function(data, statusText, xhr) {
            console.log(xhr.status);
            if (xhr.status == 200) {

                localStorage.setItem("memberId", data);

                var settings = {
                    "async": true,
                    "crossDomain": true,
                    "url": "http://localhost:8080/member/planId/" + data,
                    "method": "GET",
                    "timeout": 0,
                    "headers": {
                        "Content-Type": "application/json",
                        "Accept": "application/json"
                    },
                    "processData": false
                };
                $.ajax(settings).done(function(data, statusText, xhr) {
                    console.log(xhr.status);
                    if (xhr.status == 200) {
                        alert("Login Succesful")
                        localStorage.setItem("planId", data);
                        window.location.href = "http://localhost:8080/html/Dashboard.html";
                    }


                });



            }
            if (xhr.status == 204) {
                $("#PasswordInputText").notify(
                    "Please Enter Valid Password / Email Id", { position: "right" }
                );
            }
        });
    });




    $("#EmailInputText").keydown(function() {
        var checkMail = finalValidateEmail();
        if (checkMail == false) {
            $("#NextForPasswordOnFirstQuery").show();
            $("#PasswordContent").hide();
            clickOnFirstCheck = false;
        } else {
            if ($("#EmailInputText").val().length > 1 && clickOnFirstCheck) {
                $("#NextForPasswordOnFirstQuery").hide();
                $("#PasswordContent").show();
                clickOnFirstCheck = true;
            } else {
                $("#NextForPasswordOnFirstQuery").show();
                $("#PasswordContent").hide();
                clickOnFirstCheck = false;
            }
        }
    });

    $("#EmailInputText").keyup(function() {
        var checkMail = finalValidateEmail();
        if (checkMail == false) {
            $("#NextForPasswordOnFirstQuery").show();
            $("#PasswordContent").hide();
            clickOnFirstCheck = false;
        } else {
            if ($("#EmailInputText").val().length > 1 && clickOnFirstCheck) {
                $("#NextForPasswordOnFirstQuery").hide();
                $("#PasswordContent").show();
                clickOnFirstCheck = true;
            } else {
                $("#NextForPasswordOnFirstQuery").show();
                $("#PasswordContent").hide();
                clickOnFirstCheck = false;
            }
        }
    });


});