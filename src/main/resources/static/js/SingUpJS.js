var data = "";
var selected = -1;
var mails = ["mail.com", "ahoo.com", "b.in", "ediffmail.com"];
var firstMailEntry = false;
var planId = 1;
$(document).ready(function() {
    var settings = {
        "async": true,
        "crossDomain": true,
        "url": "http://localhost:8080/plan",
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
            var display = "";
            for (var i = 0; i < data.length; i++) {
                display += "<div class=\"Plan\"><div class=\"PlanName\"><label class=\"container\">";
                display += data[i].planName;
                display += "<input type=\"radio\" name=\"radio\" value=\"" + (i + 1) + "\" id=\"" + data[i].planName + "Radio\">";
                display += "<span class=\"checkmark\"></span></label></div><div class=\"PlanPrice\"><font color=\"goldenrod\"><b>  &#8377;</b></font>";
                display += data[i].planPrice;
                display += "</div><div class=\"PlanDescription\">";
                display += data[i].planDescription;
                display += "</div><div class=\"UploadAllowed\">";
                display += "<font color=\"blue\"><b> &#8593;</b></font>";
                if (data[i].uploadAllowed == -1) { display += "&#8734;" } else { display += data[i].uploadAllowed };
                display += "</div><div class=\"DownloadAllowed\"><font color=\"green\"><b> &#8595;</b></font>";
                if (data[i].downloadAllowed == -1) { display += "&#8734;" } else { display += data[i].downloadAllowed };
                display += "</div><div class=\"PaidAccess\"><font color=\"goldenrod\"><b>  &#8377;</b></font>";
                if (data[i].paidAllowed == -1) { display += "&#8734;" } else { display += data[i].paidAllowed };
                display += "</div></div>";
            }
            document.getElementById("Plans").innerHTML = display;

        } else {
            location.reload();
        }
    });


    $(document).on("click", "#FreeRadio", function() {
        planId = document.getElementById("FreeRadio").value;
        console.log(planId);
    });

    $(document).on("click", "#ScalesRadio", function() {
        planId = document.getElementById("ScalesRadio").value;
        console.log(planId);
    });

    $(document).on("click", "#MasterRadio", function() {
        planId = document.getElementById("MasterRadio").value;
        console.log(planId);
    });


    $("#ImageUploadedNotification").hide();

    InlineEditor
        .create(document.querySelector('#editor'), {
            toolbar: ['Heading', '|', 'bold', 'italic', 'bulletedList', 'numberedList', 'blockQuote', 'Link', 'Undo', 'Redo']
        })
        .then(editor => {
            $("#MainSubmitButtonText").hover(function() {
                document.getElementById("MainTextArea").innerHTML = editor.getData();
            });
        })
        .catch(error => {
            console.error(error);
        });

    $("#MainUserEmailIdInputText").keyup(function() {
        var inAt = false;
        var getIn = false;
        var hurrayDotPresent = false;
        var contentsAfterAt = "";
        var mailContent = document.getElementById("MainUserEmailIdInputText").value;
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
            document.getElementById("MainUserEmailIdInputText").value = "";
        }
        if (firstMailEntry == true) {
            if (contentsAfterAt == "") {
                hurrayDotPresent = false;
            } else {
                hurrayDotPresent = true;
            }
        }
        var contentsOfMail = document.getElementById("MainUserEmailIdInputText").value;
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
                var mail = document.getElementById("MainUserEmailIdInputText").value;
                document.getElementById("MainUserEmailIdInputText").value = mail + mails[0];
                firstMailEntry = true;
            } else if (event.keyCode == 89) {
                selected = 1;
                var mail = document.getElementById("MainUserEmailIdInputText").value;
                document.getElementById("MainUserEmailIdInputText").value = mail + mails[1];
                firstMailEntry = true;
            } else if (event.keyCode == 68) {
                selected = 2;
                var mail = document.getElementById("MainUserEmailIdInputText").value;
                document.getElementById("MainUserEmailIdInputText").value = mail + mails[2];
                firstMailEntry = true;
            } else if (event.keyCode == 82) {
                selected = 3;
                var mail = document.getElementById("MainUserEmailIdInputText").value;
                document.getElementById("MainUserEmailIdInputText").value = mail + mails[3];
                firstMailEntry = true;
            }
        }
    });







    //for contact field
    $("#MainUserContactNumberInputText").keyup(function() {
        if (event.keyCode != 37 && event.keyCode != 39) {
            contactLength = document.getElementById("MainUserContactNumberInputText").value.length;
            if (event.keyCode == 8 && contactLength != 0) //backspace
            {
                --contactLength;
            } else {
                //verifies that the character entered is between 0 to 9 
                if (event.keyCode == 48 || event.keyCode == 49 || event.keyCode == 50 || event.keyCode == 51 || event.keyCode == 52 || event.keyCode == 53 || event.keyCode == 54 || event.keyCode == 55 || event.keyCode == 56 || event.keyCode == 57 || event.keyCode == 96 || event.keyCode == 97 || event.keyCode == 98 || event.keyCode == 99 || event.keyCode == 100 || event.keyCode == 101 || event.keyCode == 102 || event.keyCode == 103 || event.keyCode == 104 || event.keyCode == 105) {
                    if (contactLength == 10) {
                        contactLength = 10;
                    } else {
                        ++contactLength;
                    }
                    //check for initials
                    if (contactLength == 2) {
                        //(19.06.2019) Initials of a Mobile/Cellular phone can only start from 6, 7, 8 or 9
                        if (event.keyCode == 54 || event.keyCode == 55 || event.keyCode == 56 || event.keyCode == 57 || event.keyCode == 102 || event.keyCode == 103 || event.keyCode == 104 || event.keyCode == 105) {

                        } else {
                            navigator.vibrate(250);
                            $.notify("Please do not enter Illegal Initials In Contact", "warn");
                            //deletes currently entered character(initial)
                            var contact = document.getElementById("MainUserContactNumberInputText").value;
                            var finalContact = "";
                            for (var i = 0; i < contact.length - 1; i++) {
                                finalContact += contact.charAt(i);
                            }
                            document.getElementById("MainUserContactNumberInputText").value = finalContact;
                            --contactLength;
                        }

                    }
                } else {
                    navigator.vibrate(250);
                    $.notify("Please do not enter letters in Contact", "warn");
                    //deletes currently entered character(current(this))
                    var contact = document.getElementById("MainUserContactNumberInputText").value;
                    var finalContact = "";
                    for (var i = 0; i < contact.length - 1; i++) {
                        finalContact += contact.charAt(i);
                    }
                    document.getElementById("MainUserContactNumberInputText").value = finalContact;

                }
            }
        }
    });

    $("#MainUserContactNumberInputText").keypress(function() {
        var contact = document.getElementById("MainUserContactNumberInputText").value;
        var newElement = "";
        for (var i = 0; i < contact.length; i++) {

            if (contact.charAt(0) == '6' || contact.charAt(0) == '7' || contact.charAt(0) == '8' || contact.charAt(0) == '9') {
                if (contact.charAt(i) >= '0' && contact.charAt(i) <= '9') {
                    newElement += contact.charAt(i);
                }
            } else {
                newElement += "";
            }
        }
        document.getElementById("MainUserContactNumberInputText").value = newElement;
    });



    $("#MainSubmitButtonText").click(function() {
        var checkForAllFields = checkForFields();
        if (checkForAllFields) {
            var date = (("" + new Date().toISOString()).replace("T", " ")).replace("Z", "");
            console.log(planId);
            var JSONdata = {
                "userName": document.getElementById("MainUserNameInputText").value,
                "emailId": document.getElementById("MainUserEmailIdInputText").value,
                "contactNumber": document.getElementById("MainUserContactNumberInputText").value,
                "password": document.getElementById("UserPasswordInputTextText").value,
                "securityQuestion1": document.getElementById("MainSecurityQuestion1Input").value,
                "securityQuestion2": document.getElementById("MainSecutityQuestion2Input").value,
                "securityQuestion3": document.getElementById("MainSecutityQuestion3Input").value,
                "description": document.getElementById("MainTextArea").innerHTML,
                "planId": parseInt(planId),
                "createdOn": date
            };
            var settings = {
                "async": true,
                "crossDomain": true,
                "url": "http://localhost:8080/member/singUp",
                "method": "POST",
                "headers": {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                "processData": false,
                "data": JSON.stringify(JSONdata)
            };

            $.ajax(settings).done(function(data, statusText, xhr) {
                if (data != 0) {
                    console.log("In file Upload")
                    var form = new FormData();
                    form.append("file", document.getElementById("MainUserImageFileUploadButton").files[0]);

                    var settings = {
                        "async": true,
                        "crossDomain": true,
                        "url": "http://localhost:8080/member/singUp/image/" + data,
                        "method": "POST",
                        "timeout": 0,
                        "processData": false,
                        "mimeType": "multipart/form-data",
                        "contentType": false,
                        "data": form
                    };
                    $.ajax(settings).done(function(data, statusText, xhr) {
                        if (xhr.status == 201) {
                            $("#MainSubmitButtonText").notify(
                                "You have Singed Up Successfully", { position: "right" }, "success"
                            )
                            window.location.href = "http://localhost:8080/html/Login.html";
                        }
                    });
                } else {
                    //Show Model
                }

            });
        } else {
            $.notify("Please Enter All Details", "warn");
        }

    });


    $("#MainUserImageFileUploadButton").change(function() {
        var file = document.getElementById("MainUserImageFileUploadButton");
        if (file.files.length > 0) {
            $("#ImageUploadedNotification").show();
        } else {
            $("#ImageUploadedNotification").hide();
        }
    });


    $("#MainSubmitButtonText").on("mouseover mouseenter hover click", function() {

        if (selected == -1) {
            var mailContents = document.getElementById("MainUserEmailIdInputText").value;
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
        var mailContent = document.getElementById("MainUserEmailIdInputText").value;
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
            document.getElementById("MainUserEmailIdInputText").value = newMail;
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
            document.getElementById("MainUserEmailIdInputText").value = newMail;
        }

    });


});

function checkForFields() {

    var image = checkImage();
    var username = checkUserName();
    var email = checkEmailId();
    var contactNumber = checkContactNumber();
    var password = checkPassword();
    var secuirtyQuestion1 = chechSecuirtyQuestion1();
    var secuirtyQuestion2 = chechSecuirtyQuestion2();
    var secuirtyQuestion3 = chechSecuirtyQuestion3();
    var description = chechDescription();

    if (image && username && email && contactNumber && password && secuirtyQuestion1 && secuirtyQuestion2 && secuirtyQuestion3 && description) {
        return true;
    }
    if (!image) {
        $.notify("Please upload an Image", "warn");
    }
    if (!username) {
        $.notify("Please fill UserName", "warn");
    }
    if (!email) {
        $.notify("Please fill EmailId Correctly", "warn");
    }
    if (!contactNumber) {
        $.notify("Please fill ContactNumber Correctly", "warn");
    }
    if (!password) {
        $.notify("Please fill Password Corretly", "warn");
    }
    if (!secuirtyQuestion1) {
        $.notify("Please fill SecuirtyQuestion1 ", "warn");
    }
    if (!secuirtyQuestion2) {
        $.notify("Please fill SecuirtyQuestion2", "warn");
    }
    if (!secuirtyQuestion3) {
        $.notify("Please fill SecuirtyQuestion3", "warn");
    }
    if (!description) {
        $.notify("Please fill Description ", "warn");
    }
    return false;
}

function checkImage() {
    var file = document.getElementById("MainUserImageFileUploadButton");
    if (file.files.length > 0) {
        return true;
    }
    return false;
}

function checkUserName() {
    var username = document.getElementById("MainUserNameInputText").value;
    if (username.length > 0) {
        return true;
    }
    return false;
}

function checkEmailId() {
    var email = document.getElementById("MainUserEmailIdInputText").value;
    if (email.length < 1) {
        return false;
    } else {
        var checkMailSyntax = mailSyntaxCheck();
        if (checkMailSyntax) {
            return true;
        }
    }
    return false;
}

function mailSyntaxCheck() {
    var email = document.getElementById("MainUserEmailIdInputText").value;
    var atTheRate = false;
    var dot = false;
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


function checkContactNumber() {

    var contactNumber = document.getElementById("MainUserContactNumberInputText").value;
    if (contactNumber.length < 1) {
        return false;
    } else {
        var checkContactNumber = checkContactNumberSyntax();
        if (checkContactNumber) {
            return true;
        }
    }
    return false;
}

function checkContactNumberSyntax() {
    var contact = document.getElementById("MainUserContactNumberInputText").value;
    for (var i = 0; i < 10; i++) {
        if (i == 0) {
            //verifies initials on contact field
            //(19.06.2019) only 7, 8 or 9 can be accepted as initials
            if (contact.charAt(i) == '6' || contact.charAt(i) == '7' || contact.charAt(i) == '8' || contact.charAt(i) == '9') {} else {
                return false; //initial incorrect
            }
        } else {
            //verifies for any number
            if (contact.charAt(i) == '0' || contact.charAt(i) == '1' || contact.charAt(i) == '2' || contact.charAt(i) == '3' || contact.charAt(i) == '4' || contact.charAt(i) == '5' || contact.charAt(i) == '6' || contact.charAt(i) == '7' || contact.charAt(i) == '8' || contact.charAt(i) == '9') {} else {
                return false; //any character in between incorrect
            }
        }
    }

    return true;
}

function checkPassword() {
    var password = document.getElementById("UserPasswordInputTextText").value;
    if (password.length > 9) {
        return true;
    }
    return false;
}

function chechSecuirtyQuestion1() {
    var secuirtyQuestion1 = document.getElementById("MainSecurityQuestion1Input").value;
    if (secuirtyQuestion1.length > 0) {
        return true;
    }
    return false;
}

function chechSecuirtyQuestion2() {
    var secuirtyQuestion2 = document.getElementById("MainSecutityQuestion2Input").value;
    if (secuirtyQuestion2.length > 0) {
        return true;
    }
    return false;
}

function chechSecuirtyQuestion3() {
    var secuirtyQuestion3 = document.getElementById("MainSecutityQuestion3Input").value;
    if (secuirtyQuestion3.length > 0) {
        return true;
    }
    return false;
}

function chechDescription() {
    var description = document.getElementById("MainTextArea").innerText;
    if (description.length > 0) {
        return true;
    }
    return false;
}