package main

import (
	"context"
	"fmt"
	"net/http"
	"os/exec"
	"strings"
	"text/template"
	"time"
)

type RequestHandler struct {
	mTemplates *template.Template
}

func (this RequestHandler) initialize() {
	this.mTemplates, _ = template.ParseFiles("index.gtpl")

	http.HandleFunc("/", this.index)
	http.HandleFunc("/connect", this.connect)
	http.HandleFunc("/clear_data", this.clearData)
	http.HandleFunc("/uninstall", this.uninstall)
	http.HandleFunc("/input_key_event", this.inputKeyEvent)
	http.HandleFunc("/set_tcp_mode", this.setTcpMode)
}

func (this RequestHandler) run() {
	if err := http.ListenAndServe(":17001", http.DefaultServeMux); err != nil {
		print(err.Error())
	}
}

func (this RequestHandler) index(w http.ResponseWriter, r *http.Request) {
	this.mTemplates.ExecuteTemplate(w, "index", nil)
}

func (this RequestHandler) clearData(w http.ResponseWriter, r *http.Request) {
	pkg := r.URL.Query().Get("package")

	if pkg == "" {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("{\"error\":\"missing 'package'\"}"))
		return
	}

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	cmd := exec.CommandContext(ctx, "adb", "shell", "pm", "clear", pkg)

	result, err := cmd.Output()

	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		if ctx.Err() == context.DeadlineExceeded {
			w.Write([]byte("{\"error\":\"Package does not exist.\"}"))
		} else {
			w.Write([]byte("{\"error\":\"" + err.Error() + "\"}"))
		}
		return
	}

	if strings.Contains(string(result), "Success") {
		w.Write([]byte("{\"data\":\"Data has been cleared!\"}"))
	} else {
		w.WriteHeader(http.StatusInternalServerError)
		w.Write([]byte("{\"error\":\"Failed to clear data.\"}"))
	}
}

func (this RequestHandler) uninstall(w http.ResponseWriter, r *http.Request) {
	pkg := r.URL.Query().Get("package")

	if pkg == "" {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("{\"error\":\"missing 'package'\"}"))
		return
	}

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	cmd := exec.CommandContext(ctx, "adb", "uninstall", pkg)

	result, err := cmd.Output()

	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		if ctx.Err() == context.DeadlineExceeded {
			w.Write([]byte("{\"error\":\"Package does not exist.\"}"))
		} else {
			w.Write([]byte("{\"error\":\"" + err.Error() + "\"}"))
		}
		return
	}

	if strings.Contains(string(result), "Success") {
		w.Write([]byte("{\"data\":\"Uninstalled successfully!\"}"))
	} else {
		w.WriteHeader(http.StatusInternalServerError)
		w.Write([]byte("{\"error\":\"Failed to uninstall app.\"}"))
	}
}

func (this RequestHandler) inputKeyEvent(w http.ResponseWriter, r *http.Request) {
	keyCode := r.URL.Query().Get("key_code")

	if keyCode == "" {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("{\"error\":\"missing 'key_code'\"}"))
		return
	}

	result, err := exec.Command("adb", "shell", "input", "keyevent", keyCode).Output()
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("{\"error\":\"" + err.Error() + "\"}"))
		return
	}

	if len(string(result)) == 0 {
		w.Write([]byte("{\"data\":\"Key event sent!\"}"))
	} else {
		w.WriteHeader(http.StatusInternalServerError)
		w.Write([]byte("{\"error\":\"Invalid key code.\"}"))
	}
}

func (this RequestHandler) connect(w http.ResponseWriter, r *http.Request) {
	host := r.URL.Query().Get("host")
	port := r.URL.Query().Get("port")

	if host == "" {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("{\"error\":\"missing 'host'\"}"))
		return
	}
	if port == "" {
		port = "5555"
	}

	cmd := fmt.Sprintf("adb connect %s:%s", host, port)
	print(cmd)
	result, err := exec.Command("adb", "connect", host+":"+port).Output()
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("{\"error\":\"" + err.Error() + "\"}"))
		return
	}

	if strings.Contains(string(result), "connected") {
		w.Write([]byte("{\"data\":\"" + strings.Trim(string(result), "\n") + "\"}"))
	} else {
		w.WriteHeader(http.StatusInternalServerError)
		w.Write([]byte("{\"error\":\"" + strings.Trim(string(result), "\n") + "\"}"))
	}
}

func (this RequestHandler) setTcpMode(w http.ResponseWriter, r *http.Request) {
	port := r.URL.Query().Get("port")

	if port == "" {
		port = "5555"
	}

	result, err := exec.Command("adb", "tcpip", port).Output()
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("{\"error\":\"" + err.Error() + "\"}"))
		return
	}

	if !strings.Contains(string(result), "error") {
		w.Write([]byte("{\"data\":\"TCP mode set!\"}"))
	} else {
		w.WriteHeader(http.StatusInternalServerError)
		w.Write([]byte("{\"error\":\"" + strings.Trim(string(result), "\n") + "\"}"))
	}
}

func main() {
	rh := RequestHandler{}
	rh.initialize()
	rh.run()
}
