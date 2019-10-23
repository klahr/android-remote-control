package main

import (
	"context"
	"net/http"
	"os"
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
	http.Handle("/img/", http.StripPrefix("/img/", http.FileServer(http.Dir("img/"))))
	http.HandleFunc("/connect", this.connect)
	http.HandleFunc("/clear_data", this.clearData)
	http.HandleFunc("/install", this.install)
	http.HandleFunc("/uninstall", this.uninstall)
	http.HandleFunc("/input_key_event", this.inputKeyEvent)
	http.HandleFunc("/set_tcp_mode", this.setTcpMode)
	http.HandleFunc("/start", this.start)
	http.HandleFunc("/screenshot", this.screenshot)
}

func (this RequestHandler) run() {
	print("Now browse to http://localhost:17001\n")
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

func (this RequestHandler) install(w http.ResponseWriter, r *http.Request) {
	filename := r.URL.Query().Get("filename")

	if filename == "" {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("{\"error\":\"missing 'filename'\"}"))
		return
	}

	ctx, cancel := context.WithTimeout(context.Background(), 60*time.Second)
	defer cancel()
	cmd := exec.CommandContext(ctx, "adb", "install", "-r", "-t", filename)

	result, err := cmd.Output()

	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		if ctx.Err() == context.DeadlineExceeded {
			w.Write([]byte("{\"error\":\"Invalid APK.\"}"))
		} else {
			w.Write([]byte("{\"error\":\"" + err.Error() + "\"}"))
		}
		return
	}

	if strings.Contains(string(result), "Success") {
		w.Write([]byte("{\"data\":\"Installed successfully!\"}"))
	} else {
		w.WriteHeader(http.StatusInternalServerError)
		w.Write([]byte("{\"error\":\"Failed to install APK.\"}"))
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

func (this RequestHandler) screenshot(w http.ResponseWriter, r *http.Request) {
	_, err := exec.Command("adb", "shell", "screencap", "-p", "/sdcard/screenshot.png").Output()
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("{\"error\":\"" + err.Error() + "\"}"))
		return
	}
	exec.Command("adb", "pull", "/sdcard/screenshot.png").Output()
	exec.Command("adb", "shell", "rm", "/sdcard/screenshot.png").Output()
	os.Rename("./screenshot.png", "./img/screenshot.png")

	w.Write([]byte("{\"data\":\"Screenshot captured!\"}"))
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

func (this RequestHandler) start(w http.ResponseWriter, r *http.Request) {
	activity := r.URL.Query().Get("activity")

	if activity == "" {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("{\"error\":\"missing 'activity'\"}"))
		return
	}

	result, err := exec.Command("adb", "shell", "am", "start", "-n", activity).Output()
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("{\"error\":\"" + err.Error() + "\"}"))
		return
	}

	if !strings.Contains(string(result), "Error") {
		w.Write([]byte("{\"data\":\"Activity started!\"}"))
	} else {
		w.WriteHeader(http.StatusInternalServerError)
		w.Write([]byte("{\"error\":\"Failed to start activity.\"}"))
	}
}

func main() {
	rh := RequestHandler{}
	rh.initialize()
	rh.run()
}
