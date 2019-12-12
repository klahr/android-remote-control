{{define "index"}}
<html>

<head>
    <style>
        body {
            background-color: #3e434c;
        }

        table {
            background-color: #3c3f41;
            border-radius: 12px;
            border: 4px solid #2b2b2b;
            padding: 16px;
        }

        p {
            font-family: sans-serif;
            font-size: 14px;
            font-weight: bold;
            color: #bbbbbb;
        }

        h1 {
            font-family: sans-serif;
            margin-top: 24px;
            color: #499c54;
        }

        input {
            padding: 4px;
            font-family: monospace;
            background-color: #2b2b2b;
            color: #a6b5bf;
            border: none;
            width: 100%;
        }

        textarea {
            padding: 4px;
            font-family: monospace;
            resize: none;
            background-color: #2b2b2b;
            color: #a6b5bf;
            border: none;
        }

        button {
            padding: 8px;
            background-color: #365880;
            border: 2px solid #4c708c;
            border-radius: 4px;
            color: #babab6;
            font-weight: bold;
            font-size: 14px;
        }

        button:active {
            border: 2px solid #43688c;
            box-shadow: inset white 0 0 2px;
        }

    </style>
</head>

<body>

<script type="text/javascript">
    function onTcpMode() {
        var port = document.getElementById("tcp_mode_port").value;

        localStorage.setItem("tcp_mode_port", port);

        result.style.color = "white";
        result.innerHTML = "Setting...";

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function(data) {
            if (this.readyState == 4) {
                var result = document.getElementById("result");
                var json = JSON.parse(this.responseText);
                if (this.status == 200) {
                    result.style.color = "#499c54";
                    result.innerHTML = json.data;
                } else {
                    result.style.color = "red";
                    result.innerHTML = "ERROR: " + json.error;
                }
            }
        };
        xhttp.open("POST", `/set_tcp_mode?port=${port}`, true);
        xhttp.send(); 
    }

    function onConnect() {
        var host = document.getElementById("connect_host").value;
        var port = document.getElementById("connect_port").value;

        localStorage.setItem("connect_host", host);
        localStorage.setItem("connect_port", port);

        result.style.color = "white";
        result.innerHTML = "Connecting...";

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function(data) {
            if (this.readyState == 4) {
                var result = document.getElementById("result");
                var json = JSON.parse(this.responseText);
                if (this.status == 200) {
                    result.style.color = "#499c54";
                    result.innerHTML = json.data;
                } else {
                    result.style.color = "red";
                    result.innerHTML = "ERROR: " + json.error;
                }
            }
        };
        xhttp.open("POST", `/connect?host=${host}&port=${port}`, true);
        xhttp.send(); 
    }

    function onClearData() {
        var package = document.getElementById("clear_data_package").value;

        localStorage.setItem("clear_data_package", package);

        result.style.color = "white";
        result.innerHTML = "Clearing data...";

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function(data) {
            if (this.readyState == 4) {
                var result = document.getElementById("result");
                var json = JSON.parse(this.responseText);
                if (this.status == 200) {
                    result.style.color = "#499c54";
                    result.innerHTML = json.data;
                } else {
                    result.style.color = "red";
                    result.innerHTML = "ERROR: " + json.error;
                }
            }
        };
        xhttp.open("POST", `/clear_data?package=${package}`, true);
        xhttp.send(); 
    }

    function onUninstall() {
        var package = document.getElementById("uninstall_package").value;

        localStorage.setItem("uninstall_package", package);

        result.style.color = "white";
        result.innerHTML = "Uninstalling...";

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function(data) {
            if (this.readyState == 4) {
                var result = document.getElementById("result");
                var json = JSON.parse(this.responseText);
                if (this.status == 200) {
                    result.style.color = "#499c54";
                    result.innerHTML = json.data;
                } else {
                    result.style.color = "red";
                    result.innerHTML = "ERROR: " + json.error;
                }
            }
        };
        xhttp.open("POST", `/uninstall?package=${package}`, true);
        xhttp.send(); 
    }

    function onInstall() {
        var filename = document.getElementById("install_filename").value;

        localStorage.setItem("install_filename", filename);

        result.style.color = "white";
        result.innerHTML = `Installing '${filename}'...`;

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function(data) {
            if (this.readyState == 4) {
                var result = document.getElementById("result");
                var json = JSON.parse(this.responseText);
                if (this.status == 200) {
                    result.style.color = "#499c54";
                    result.innerHTML = json.data;
                } else {
                    result.style.color = "red";
                    result.innerHTML = "ERROR: " + json.error;
                }
            }
        };
        xhttp.open("POST", `/install?filename=${filename}`, true);
        xhttp.send(); 
    }

    function onStartActivity() {
        var activity = document.getElementById("start_activity_activity").value;

        localStorage.setItem("start_activity_activity", activity);

        result.style.color = "white";
        result.innerHTML = "Starting activity...";

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function(data) {
            if (this.readyState == 4) {
                var result = document.getElementById("result");
                var json = JSON.parse(this.responseText);
                if (this.status == 200) {
                    result.style.color = "#499c54";
                    result.innerHTML = json.data;
                } else {
                    result.style.color = "red";
                    result.innerHTML = "ERROR: " + json.error;
                }
            }
        };
        xhttp.open("POST", `/start?activity=${activity}`, true);
        xhttp.send(); 
    }

    function onScreenshot() {
        result.style.color = "white";
        result.innerHTML = "Capturing screenshot...";

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function(data) {
            if (this.readyState == 4) {
                var result = document.getElementById("result");
                var json = JSON.parse(this.responseText);
                if (this.status == 200) {
                    result.style.color = "#499c54";
                    result.innerHTML = json.data;
                    document.getElementById("image_result").style.display = "block";
                    document.getElementById("image_result").src = "img/screenshot.png?t=" + new Date().getTime();
                } else {
                    result.style.color = "red";
                    result.innerHTML = "ERROR: " + json.error;
                }
            }
        };
        xhttp.open("POST", `/screenshot`, true);
        xhttp.send(); 
    }

    function onInputKeyEvent(keyCode) {
        if (keyCode === null) {
            keyCode = document.getElementById("key_code").value;
        }

        result.style.color = "white";
        result.innerHTML = "Sending input key event...";

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function(data) {
            if (this.readyState == 4) {
                var result = document.getElementById("result");
                var json = JSON.parse(this.responseText);
                if (this.status == 200) {
                    result.style.color = "#499c54";
                    result.innerHTML = json.data;
                } else {
                    result.style.color = "red";
                    result.innerHTML = "ERROR: " + json.error;
                }
                onScreenshot();
            }
        };
        xhttp.open("POST", `/input_key_event?key_code=${keyCode}`, true);
        xhttp.send(); 
    }

    function onInputTapEvent(x, y) {
        result.style.color = "white";
        result.innerHTML = "Sending tap (" + x + ", " + y + ") event...";

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function(data) {
            if (this.readyState == 4) {
                var result = document.getElementById("result");
                var json = JSON.parse(this.responseText);
                if (this.status == 200) {
                    result.style.color = "#499c54";
                    result.innerHTML = json.data;
                } else {
                    result.style.color = "red";
                    result.innerHTML = "ERROR: " + json.error;
                }
                onScreenshot();
            }
        };
        xhttp.open("POST", `/input_tap_event?x=${x}&y=${y}`, true);
        xhttp.send();
    }

    function onText(event) {
        event.preventDefault();
        document.getElementById("text").value = "";
        if (event.keyCode >= 65 && event.keyCode <= 90) {
            onInputKeyEvent(event.keyCode - 36);
        } else if (event.keyCode >= 48 && event.keyCode <= 57) {
            onInputKeyEvent(event.keyCode - 41);
        } else if (event.keyCode >= 112 && event.keyCode <= 123) {
            onInputKeyEvent(event.keyCode + 19)
        } else {
            switch (event.keyCode) {
                case 32: onInputKeyEvent(62); break;
                case 8: onInputKeyEvent(67); break;
                case 13: onInputKeyEvent(66); break;
                case 37: onInputKeyEvent(21); break;
                case 38: onInputKeyEvent(19); break;
                case 39: onInputKeyEvent(22); break;
                case 40: onInputKeyEvent(20); break;
                case 27: onInputKeyEvent(111); break;
                case 9: onInputKeyEvent(61); break;
                case 190: onInputKeyEvent(56); break;
                case 188: onInputKeyEvent(55); break;
            }
        }
    }

    function onScreenshotClick(e) {
        var x;
        var y;
        if (e.pageX || e.pageY) { 
            x = e.pageX;
            y = e.pageY;
        }
        else { 
            x = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft; 
            y = e.clientY + document.body.scrollTop + document.documentElement.scrollTop; 
        } 

        x -= document.getElementById("image_result").offsetLeft;
        y -= document.getElementById("image_result").offsetTop;

        onInputTapEvent(x, y);

        document.getElementById("text").select();
    }

</script>

<center>

<h1>Android Remote Control</h1>

<div>
    <table>
        <tr>
            <td>
                <p>TCP Mode</p>
            </td>
            <td>
                <input value="localhost" disabled></input>
            </td>
            <td>
                <input id="tcp_mode_port" placeholder="Port" value="5555"></input>
            </td>
            <td>
                <button onclick="onTcpMode()">SET</button>
            </td>
        </tr>
        <tr>
            <td>
                <p>Connect</p>
            </td>
            <td>
                <input id="connect_host" placeholder="192.168.0.1"></input>
            </td>
            <td>
                <input id="connect_port" placeholder="Port" value="5555"></input>
            </td>
            <td>
                <button onclick="onConnect()">CONNECT</button>
            </td>
        </tr>
        <tr>
            <td>
                <p>Start activity</p>
            </td>
            <td colspan="2">
                <input id="start_activity_activity" placeholder="com.example.app/com.example.app.MainActivity"></input>
            </td>
            <td>
                <button onclick="onStartActivity()">START</button>
            </td>
        </tr>
        <tr>
            <td>
                <p>Clear data</p>
            </td>
            <td colspan="2">
                <input id="clear_data_package" placeholder="com.example.app"></input>
            </td>
            <td>
                <button onclick="onClearData()">CLEAR DATA</button>
            </td>
        </tr>
        <tr>
            <td>
                <p>Install</p>
            </td>
            <td colspan="2">
                <input id="install_filename" placeholder="/home/user/file.apk"></input>
            </td>
            <td>
                <button onclick="onInstall()">INSTALL</button>
            </td>
        </tr>
        <tr>
            <td>
                <p>Uninstall</p>
            </td>
            <td colspan="2">
                <input id="uninstall_package" placeholder="com.example.app"></input>
            </td>
            <td>
                <button onclick="onUninstall()">UNINSTALL</button>
            </td>
        </tr>
        <tr>
            <td colspan="3">
                <p>Screenshot</p>
            </td>
            <td>
                <button onclick="onScreenshot()">CAPTURE</button>
            </td>
        </tr>
        <tr>
            <td>
                <p>Key code</p>
            </td>
            <td colspan="2">
                <input id="key_code" style="width: 100%;"></input>
            </td>
            <td>
                <button onclick="onInputKeyEvent(null)">SEND</button>
            </td>
        </tr>
        <tr>
            <td>
                <p>Text</p>
            </td>
            <td colspan="2">
                <input id="text" autocomplete="off" placeholder="Place marker here to send key events" onkeydown="onText(event)"></input>
            </td>
            <td>
                <button onclick="onInputKeyEvent(4)">BACK</button>
                <button onclick="onInputKeyEvent(3)">HOME</button>
            </td>
        </tr>
    </table>
</div>

<p id="result"></p>
<img id="image_result" onclick="onScreenshotClick(event)" onerror="this.style.display='none'" style="border: 24px solid #000; border-radius: 32px;" src="img/screenshot.png" />

</center>

</body>

<script>
    if (localStorage.getItem("tcp_mode_port") != null) {
        document.getElementById("tcp_mode_port").value = localStorage.getItem("tcp_mode_port");
    }
    document.getElementById("connect_host").value = localStorage.getItem("connect_host");
    document.getElementById("connect_port").value = localStorage.getItem("connect_port");
    document.getElementById("start_activity_activity").value = localStorage.getItem("start_activity_activity");
    document.getElementById("clear_data_package").value = localStorage.getItem("clear_data_package");
    document.getElementById("uninstall_package").value = localStorage.getItem("uninstall_package");
    document.getElementById("install_filename").value = localStorage.getItem("install_filename");
</script>

</html>

{{end}}
