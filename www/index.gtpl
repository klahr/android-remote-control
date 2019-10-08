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
            font-family: verdana;
            font-size: 14px;
            font-weight: bold;
            color: #bbbbbb;
        }

        h1 {
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
        var port = document.getElementById("port").value;

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
        var host = document.getElementById("host").value;
        var port = document.getElementById("port").value;

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
        var package = document.getElementById("clear_data").value;

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
        var package = document.getElementById("uninstall").value;

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
            }
        };
        xhttp.open("POST", `/input_key_event?key_code=${keyCode}`, true);
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
                <input id="port" placeholder="port" value="5555"></input>
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
                <input id="host" placeholder="host" value="192.168.0.55"></input>
            </td>
            <td>
                <input id="port" placeholder="port" value="5555"></input>
            </td>
            <td>
                <button onclick="onConnect()">CONNECT</button>
            </td>
        </tr>
        <tr>
            <td>
                <p>Clear data</p>
            </td>
            <td colspan="2">
                <input id="clear_data" placeholder="com.example.app"></input>
            </td>
            <td>
                <button onclick="onClearData()">CLEAR DATA</button>
            </td>
        </tr>
        <tr>
            <td>
                <p>Uninstall</p>
            </td>
            <td colspan="2">
                <input id="uninstall" placeholder="com.example.app"></input>
            </td>
            <td>
                <button onclick="onUninstall()">UNINSTALL</button>
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
                <input id="text" placeholder="Place marker here to send key events" onkeydown="onText(event)"></input>
            </td>
            <td>
                <button onclick="onInputKeyEvent(4)">BACK</button>
                <button onclick="onInputKeyEvent(3)">HOME</button>
            </td>
        </tr>
    </table>
</div>

<p id="result"></p>

</center>

</body>
</html>

{{end}}
