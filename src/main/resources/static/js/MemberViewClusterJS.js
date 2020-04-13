$(document).ready(function() {
    if (localStorage.getItem("planId") != 1) {
        var displayHeader = "                <div class=\"subbordinateActivities\">\r\n" +
            "                    <a href=\"http://localhost:8080/html/UploadDocument.html\" id=\"uploadDocument\" style=\"text-decoration:none;color:rgb(154, 231, 154);\">Upload Document</a>\r\n" +
            "                </div>";
        document.getElementById("subbordinateActivity").innerHTML += displayHeader;
    }
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
        if (xhr.status == 200) {
            var display = "";
            for (var i = 0; i < data.length; i++) {
                var accessColor = "rgb(46, 44, 44);";
                var accessText = "SUBSCRIBE";
                if (i % 3 == 0 || i == 0) {
                    display += "<div class=\"Section\">\r\n" +
                        "                    <div class=\"SectionContent\">";
                }
                if (data[i].currentMemberAcess) {
                    accessColor = "rgb(47, 95, 47);";
                    accessText = "SUBSCRIBED";
                }

                display += "        <div class=\"SectionContentDivision\">\r\n";
                display += "<div class=\"MemberId\" hidden>" + data[i].memberId + "</div>\n";
                display += "            <div class=\"MemberName\">\r\n" +
                    "                <div class=\"MainMemberName\">\r\n" +
                    "                    " + data[i].memberName + "\r\n" +
                    "                </div>\r\n" +
                    "            </div>\r\n" +
                    "            <div class=\"MemberNumberOfDocuments\">\r\n" +
                    "                <div class=\"MemeberUploadDocumentText\">\r\n" +
                    "                    Number of Uploads\r\n" +
                    "                </div>\r\n" +
                    "                <div class=\"NumberOfUploadDocument\">\r\n" +
                    "                    " + data[i].numberOfUploads + "\r\n" +
                    "                </div>\r\n" +
                    "            </div>\r\n" +
                    "            <div class=\"NumberOfSubscribers\">\r\n" +
                    "                <div class=\"NumberOfSubscribersText\">\r\n" +
                    "                    Number of Subscribers\r\n" +
                    "                </div>\r\n" +
                    "                <div class=\"MainNumberOfSubscribers\">\r\n" +
                    "                    " + data[i].numberOfSubscribers + "\r\n" +
                    "                </div>\r\n" +
                    "            </div>\r\n" +
                    "            <div class=\"SubscriptionButton\">\r\n" +
                    "                <div class=\"MainSubscriptionButton\" style=\" border-color: " + accessColor + ";\">\r\n" +
                    "                    " + accessText + "\r\n" +
                    "                </div>\r\n" +
                    "            </div>\r\n" +
                    "        </div>";
                if ((i + 1) % 3 == 0 && i != 0 || i == data.length - 1) {
                    console.log("Here");
                    display += "                    </div>\r\n" +
                        "                </div>";
                }

            }
            console.log(data);
            document.getElementById("Contents").innerHTML = display;

            $("#SearchBarInput").keydown(function() {
                if (event.keyCode == 13) {
                    var searchText = document.getElementById("SearchBarInput").value;
                    if (searchText == "") {
                        location.reload();
                    }
                    var searchData = [];
                    for (var i = 0; i < data.length; i++) {
                        var memberNameRs = data[i].memberName.toString();
                        if (memberNameRs == searchText) {
                            searchData.push(data[i]);
                        }
                    }

                    var display = "";
                    for (var i = 0; i < searchData.length; i++) {
                        var accessColor = "rgb(46, 44, 44);";
                        var accessText = "SUBSCRIBE";
                        if (i % 3 == 0 || i == 0) {
                            display += "<div class=\"Section\">\r\n" +
                                "                    <div class=\"SectionContent\">";
                        }
                        if (searchData[i].currentMemberAcess) {
                            accessColor = "rgb(47, 95, 47);";
                            accessText = "SUBSCRIBED";
                        }

                        display += "        <div class=\"SectionContentDivision\">\r\n";
                        display += "<div class=\"MemberId\" hidden>" + searchData[i].memberId + "</div>\n";
                        display += "            <div class=\"MemberName\">\r\n" +
                            "                <div class=\"MainMemberName\">\r\n" +
                            "                    " + searchData[i].memberName + "\r\n" +
                            "                </div>\r\n" +
                            "            </div>\r\n" +
                            "            <div class=\"MemberNumberOfDocuments\">\r\n" +
                            "                <div class=\"MemeberUploadDocumentText\">\r\n" +
                            "                    Number of Uploads\r\n" +
                            "                </div>\r\n" +
                            "                <div class=\"NumberOfUploadDocument\">\r\n" +
                            "                    " + searchData[i].numberOfUploads + "\r\n" +
                            "                </div>\r\n" +
                            "            </div>\r\n" +
                            "            <div class=\"NumberOfSubscribers\">\r\n" +
                            "                <div class=\"NumberOfSubscribersText\">\r\n" +
                            "                    Number of Subscribers\r\n" +
                            "                </div>\r\n" +
                            "                <div class=\"MainNumberOfSubscribers\">\r\n" +
                            "                    " + searchData[i].numberOfSubscribers + "\r\n" +
                            "                </div>\r\n" +
                            "            </div>\r\n" +
                            "            <div class=\"SubscriptionButton\">\r\n" +
                            "                <div class=\"MainSubscriptionButton\" style=\" border-color: " + accessColor + ";\">\r\n" +
                            "                    " + accessText + "\r\n" +
                            "                </div>\r\n" +
                            "            </div>\r\n" +
                            "        </div>";
                        if ((i + 1) % 3 == 0 && i != 0 || i == searchData.length - 1) {
                            console.log("Here");
                            display += "                    </div>\r\n" +
                                "                </div>";
                        }

                    }


                    document.getElementById("Contents").innerHTML = display;

                }
            });


            $(document).on("click", ".MainSubscriptionButton", function(e) {
                var memberId = e.target.parentElement.parentElement.firstElementChild.innerText;
                var memberText = e.target.innerText;
                if (memberText == "SUBSCRIBE") {
                    var settings = {
                        "async": true,
                        "crossDomain": true,
                        "url": "http://localhost:8080/member/subscribe/" + localStorage.getItem("memberId") + "/" + memberId,
                        "method": "POST",
                        "timeout": 0,
                        "headers": {
                            "Content-Type": "application/json",
                            "Accept": "application/json"
                        },
                        "processData": false
                    };
                    $.ajax(settings).done(function(data, statusText, xhr) {
                        if (xhr.status == 201) {
                            window.location.reload();
                        }
                        if (xhr.status == 400) {
                            alert("Sorry Your Plan does not Promote you to Subscribe Futher");
                        }
                    });
                }
            });


            $(document).on("click", ".SectionContentDivision", function(e) {
                var memberId = e.target.firstElementChild.innerText;
                sessionStorage.setItem("URLToRequest", "http://localhost:8080/document/member/" + memberId + "/" + localStorage.getItem("memberId"))
                window.location.href = "http://localhost:8080/html/DocumentViewCluster.html";
            });


            $("#circuralActivity").click(function() {
                localStorage.setItem("memberId", "");
                localStorage.setItem("planId", "");
                sessionStorage.setItem("URLToRequest", "");
                window.location.href = "http://localhost:8080/html/Login.html";
            });
            $(document.body).on('mouseenter', ".SectionContentDivision", function() {
                $(this).css("border-width", "3px");
            });
            $(document.body).on('mouseleave', ".SectionContentDivision", function() {
                $(this).css("border-width", "1px");
            });

        }
    });


});