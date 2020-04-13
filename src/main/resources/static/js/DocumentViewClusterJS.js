$(document).ready(function() {
    console.log(localStorage.getItem("planId"));
    if (localStorage.getItem("planId") != 1) {
        var displayHeader = "                <div class=\"subbordinateActivities\">\r\n" +
            "                    <div id=\"uploadDocument\" style=\"text-decoration:none;color:rgb(154, 231, 154);\">Upload Document</div>\r\n" +
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
                var accessColor = "rgb(243, 67, 67)";
                var accessText = "NO ACCESS";
                if (i % 3 == 0 || i == 0) {
                    display += "<div class=\"Section\">\r\n" +
                        "                    <div class=\"SectionContent\">";
                }
                if (data[i].currentMemberAcess) {
                    accessColor = "rgb(95, 204, 104)";
                    accessText = "ACCESSIBLE";
                }

                display += "                <div class=\"SectionContentDivision\">\r\n";
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
                console.log("i/3: " + ((i + 1) / 3 == 1 && i != 0) + "  i/3: " + (i + 1) / 3 + " i: " + i);
                if ((i + 1) % 3 == 0 && i != 0 || i == data.length - 1) {
                    console.log("Here");
                    display += "                    </div>\r\n" +
                        "                </div>";
                }
            }
            console.log(display);
            document.getElementById("Content").innerHTML = display;


            $("#SearchBarInput").keydown(function() {
                if (event.keyCode == 13) {
                    var searchText = document.getElementById("SearchBarInput").value;
                    if (searchText == "") {
                        location.reload();
                    }
                    var searchData = [];
                    for (var i = 0; i < data.length; i++) {
                        var documentNameRs = data[i].documentName.toString();
                        if (documentNameRs == searchText) {
                            searchData.push(data[i]);
                        }
                    }
                    var display = "";
                    for (var i = 0; i < searchData.length; i++) {
                        var accessColor = "rgb(243, 67, 67)";
                        var accessText = "NO ACCESS";
                        if (i % 3 == 0 || i == 0) {
                            display += "<div class=\"Section\">\r\n" +
                                "                    <div class=\"SectionContent\">";
                        }
                        if (searchData[i].currentMemberAcess) {
                            accessColor = "rgb(95, 204, 104)";
                            accessText = "ACCESSIBLE";
                        }

                        display += "                <div class=\"SectionContentDivision\">\r\n";
                        display += "<div class=\"DocumentId\" hidden>" + searchData[i].documentId + "</div>\n";
                        display += "                            <div class=\"DocumentName\">\r\n" +
                            "                                <div class=\"MainDocumentName\">\r\n" +
                            "                                    " + searchData[i].documentName + "\r\n" +
                            "                                </div>\r\n" +
                            "                            </div>\r\n" +
                            "                            <div class=\"EconomicAccess\">\r\n" +
                            "                                <div class=\"EconomicAccessText\">\r\n" +
                            "                                    Economic Access\r\n" +
                            "                                </div>\r\n" +
                            "                                <div class=\"EconomicAccessPrice\">\r\n" +
                            "                                    <font color=\"goldenrod\"> &#8377;</font> " + searchData[i].economicAccess + "\r\n" +
                            "                                </div>\r\n" +
                            "                            </div>\r\n" +
                            "                            <div class=\"MemberAccess\">\r\n" +
                            "                                <div class=\"MemberAccessText\">\r\n" +
                            "                                    Members who have it's access\r\n" +
                            "                                </div>\r\n" +
                            "                                <div class=\"TotalMemberAccess\">\r\n" +
                            "                                    " + searchData[i].memberViewAccess + "\r\n" +
                            "                                </div>\r\n" +
                            "                            </div>\r\n" +
                            "                            <div class=\"AcceesButton\">\r\n" +
                            "                                <div class=\"MainAccessButton\" style=\" border-color:" + accessColor + ";\">\r\n" +
                            "                                    " + accessText + "\r\n" +
                            "                                </div>\r\n" +
                            "                            </div>\r\n" +
                            "                        </div>";
                        console.log("i/3: " + ((i + 1) / 3 == 1 && i != 0) + "  i/3: " + (i + 1) / 3 + " i: " + i);
                        if ((i + 1) % 3 == 0 && i != 0 || i == searchData.length - 1) {
                            display += "                    </div>\r\n" +
                                "                </div>";
                        }
                    }
                    document.getElementById("Content").innerHTML = display;

                }
            });

            $(document).on("click", ".SectionContentDivision", function(e) {
                var documentId = e.target.firstElementChild.innerText;
                sessionStorage.setItem("URLToRequest", "http://localhost:8080/document/" + localStorage.getItem("memberId") + "/" + documentId)
                window.location.href = "http://localhost:8080/html/DocumentView.html";

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