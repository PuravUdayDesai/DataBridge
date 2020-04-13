$(document).ready(function() {

    if (localStorage.getItem("planId") != 1) {
        var displayHeader = "                <div class=\"subbordinateActivities\">\r\n" +
            "                    <a href=\"http://localhost:8080/html/UploadDocument.html\" id=\"uploadDocument\" style=\"text-decoration:none;color:rgb(154, 231, 154);\">Upload Document</a>\r\n" +
            "                </div>";
        document.getElementById("subbordinateActivity").innerHTML += displayHeader;
    }
    var display = "";
    var settings = {
        "async": true,
        "crossDomain": true,
        "url": "http://localhost:8080/member/top3/" + localStorage.getItem("memberId"),
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
            display += "<div class=\"Section\">\r\n";
            display += "    <div class=\"SectionName\">\r\n" +
                "        <div class=\"SectionNameAlign\">\r\n" +
                "            Upvoted Members\r\n" +
                "        </div>\r\n" +
                "    </div>";
            display += "                    <div class=\"SectionContent\">";
            for (var i = 0; i < data.length; i++) {
                var accessColor = "rgb(46, 44, 44);";
                var accessText = "SUBSCRIBE";

                if (data[i].currentMemberAcess) {
                    accessColor = "rgb(47, 95, 47);";
                    accessText = "SUBSCRIBED";
                }
                if (data[i].memberId == localStorage.getItem("memberId")) {
                    accessColor = "rgb(115, 132, 199);";
                    accessText = "SELF"
                }

                display += "        <div class=\"SectionContentDivision members\">\r\n";
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

            }
            display += "        <div class=\"SectionShowAll\">\r\n";
            display += "<div id=\"sectionId\" hidden>1</div>";
            display += "            <div class=\"SectionShowAllText\">\r\n" +
                "                >\r\n" +
                "            </div>\r\n" +
                "        </div>";
            display += "                    </div>\r\n" +
                "                </div>";
        }
        if (xhr.status == 204) {
            display = "No Content Avaliable";
        }
        document.getElementById("MainDashboardContent").innerHTML = display;
    });
    //----------------------------------------------------------------------------------------------------------------------------------
    var settings = {
        "async": true,
        "crossDomain": true,
        "url": "http://localhost:8080/document/bestseller/top3/" + localStorage.getItem("memberId"),
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
            display += "<div class=\"Section\">\r\n";
            display += "    <div class=\"SectionName\">\r\n" +
                "        <div class=\"SectionNameAlign\">\r\n" +
                "            Documents you must see\r\n" +
                "        </div>\r\n" +
                "    </div>";
            display += "                    <div class=\"SectionContent\">";
            for (var i = 0; i < data.length; i++) {
                var accessColor = "rgb(243, 67, 67)";
                var accessText = "NO ACCESS";
                if (data[i].currentMemberAcess) {
                    accessColor = "rgb(95, 204, 104)";
                    accessText = "ACCESSIBLE";
                }

                display += "        <div class=\"SectionContentDivision documents\">\r\n";
                display += "<div class=\"DocumentId\" hidden>" + data[i].documentId + "</div>\n";
                display += "                            <div class=\"DocumentName\">\r\n" +
                    "                                <div class=\"MainDocumentName\">\r\n" +
                    "                                    " + data[i].documentName + "\r\n" +
                    "                                </div>\r\n" +
                    "                            </div>\r\n" +
                    "                            <div class=\"EconomicAccess\">\r\n" +
                    "                                <div class=\"EconomicAccessText\">\r\n" +
                    "                                    Economic Access\r\n" +
                    "                                </div>\r\n" +
                    "                                <div class=\"EconomicAccessPrice\">\r\n" +
                    "                                    <font color=\"goldenrod\"> &#8377;</font> " + data[i].economicAccess + "\r\n" +
                    "                                </div>\r\n" +
                    "                            </div>\r\n" +
                    "                            <div class=\"MemberAccess\">\r\n" +
                    "                                <div class=\"MemberAccessText\">\r\n" +
                    "                                    Members who have it's access\r\n" +
                    "                                </div>\r\n" +
                    "                                <div class=\"TotalMemberAccess\">\r\n" +
                    "                                    " + data[i].memberViewAccess + "\r\n" +
                    "                                </div>\r\n" +
                    "                            </div>\r\n" +
                    "                            <div class=\"AcceesButton\">\r\n" +
                    "                                <div class=\"MainAccessButton\" style=\" border-color:" + accessColor + ";\">\r\n" +
                    "                                    " + accessText + "\r\n" +
                    "                                </div>\r\n" +
                    "                            </div>\r\n" +
                    "                        </div>";
            }
            display += "        <div class=\"SectionShowAll\">\r\n";
            display += "<div id=\"sectionId\" hidden>2</div>";
            display += "            <div class=\"SectionShowAllText\">\r\n" +
                "                >\r\n" +
                "            </div>\r\n" +
                "        </div>";
            display += "                    </div>\r\n" +
                "                </div>";


        }
        if (xhr.status == 204) {
            display = "No Content Avaliable";
        }
        document.getElementById("MainDashboardContent").innerHTML = display;


    });
    $(document).on("click", ".SectionContentDivision.documents", function(e) {
        var documentId = e.target.firstElementChild.innerText;
        sessionStorage.setItem("URLToRequest", "http://localhost:8080/document/" + localStorage.getItem("memberId") + "/" + documentId)
        window.location.href = "http://localhost:8080/html/DocumentView.html";
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


    $(document).on("click", ".SectionContentDivision.members", function(e) {
        var memberId = e.target.firstElementChild.innerText;
        sessionStorage.setItem("URLToRequest", "http://localhost:8080/document/member/" + memberId + "/" + localStorage.getItem("memberId"))
        window.location.href = "http://localhost:8080/html/DocumentViewCluster.html";
    });


    $(document).on("click", ".SectionShowAll", function(e) {
        var sectionId = e.target.firstElementChild.innerText;
        if (sectionId == 1) {
            sessionStorage.setItem("URLToRequest", "http://localhost:8080/member/top15/" + localStorage.getItem("memberId"));
            window.location.href = "http://localhost:8080/html/MemberViewCluster.html";
        } else {
            sessionStorage.setItem("URLToRequest", "http://localhost:8080/document/bestseller/top15/" + localStorage.getItem("memberId"));
            window.location.href = "http://localhost:8080/html/DocumentViewCluster.html";
        }
    });

    $("#allMembers").click(function() {
        sessionStorage.setItem("URLToRequest", "http://localhost:8080/member/" + localStorage.getItem("memberId"));
        window.location.href = "http://localhost:8080/html/MemberViewCluster.html";
    });
    $("#members").click(function() {
        sessionStorage.setItem("URLToRequest", "http://localhost:8080/member/memberAccess/" + localStorage.getItem("memberId"));
        window.location.href = "http://localhost:8080/html/MemberViewCluster.html";
    });
    $("#documents").click(function() {
        sessionStorage.setItem("URLToRequest", "http://localhost:8080/document/documentAccess/" + localStorage.getItem("memberId"));
        window.location.href = "http://localhost:8080/html/DocumentViewCluster.html";
    });
    $("#uploadDocument").click(function() {
        window.location.href = "http://localhost:8080/html/UploadDocument.html";
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


});