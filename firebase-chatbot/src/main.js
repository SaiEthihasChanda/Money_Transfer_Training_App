import * as webllm from "@mlc-ai/web-llm";

// A curated shortlist of prebuilt WebLLM models, smallest to largest,
// all comfortably under the 10GB Firebase Hosting storage budget (they
// aren't even stored on Firebase Hosting -- they're downloaded straight
// from Hugging Face's CDN into the browser's cache the first time a
// visitor loads them). Sizes below are approximate GPU memory / download
// footprints for the quantized weights.
const CURATED_MODEL_IDS = [
  "Llama-3.2-1B-Instruct-q4f16_1-MLC",
  "Qwen2.5-1.5B-Instruct-q4f16_1-MLC",
  "Llama-3.2-3B-Instruct-q4f16_1-MLC",
  "Qwen2.5-3B-Instruct-q4f16_1-MLC",
  "Phi-3.5-mini-instruct-q4f16_1-MLC",
  "Mistral-7B-Instruct-v0.3-q4f16_1-MLC",
  "Qwen2.5-7B-Instruct-q4f16_1-MLC",
  "Llama-3.1-8B-Instruct-q4f16_1-MLC",
];

const DEFAULT_MODEL_ID = "Llama-3.1-8B-Instruct-q4f16_1-MLC";

const SYSTEM_PROMPT =
  "You are a helpful, friendly, and concise assistant running entirely " +
  "inside the user's web browser.";

const modelSelect = document.getElementById("model-select");
const loadBtn = document.getElementById("load-btn");
const progressWrap = document.getElementById("progress-wrap");
const progressFill = document.getElementById("progress-fill");
const progressText = document.getElementById("progress-text");
const statusEl = document.getElementById("status");
const setupSection = document.getElementById("setup");
const chatSection = document.getElementById("chat");
const messagesEl = document.getElementById("messages");
const chatForm = document.getElementById("chat-form");
const chatInput = document.getElementById("chat-input");
const sendBtn = document.getElementById("send-btn");
const resetBtn = document.getElementById("reset-btn");

let engine = null;
let history = [{ role: "system", content: SYSTEM_PROMPT }];
let generating = false;

function setStatus(text, isError = false) {
  statusEl.textContent = text;
  statusEl.classList.toggle("error", isError);
}

function populateModelSelect() {
  const known = new Map(
    webllm.prebuiltAppConfig.model_list.map((m) => [m.model_id, m]),
  );
  const available = CURATED_MODEL_IDS.filter((id) => known.has(id)).sort(
    (a, b) => (known.get(a).vram_required_MB || 0) - (known.get(b).vram_required_MB || 0),
  );

  if (available.length === 0) {
    // Fallback in case the curated ids ever drift from the library's list.
    available.push(
      ...webllm.prebuiltAppConfig.model_list
        .filter((m) => (m.vram_required_MB || 0) > 0 && m.vram_required_MB <= 10000)
        .slice(0, 8)
        .map((m) => m.model_id),
    );
  }

  for (const id of available) {
    const record = known.get(id);
    const gb = record ? (record.vram_required_MB / 1024).toFixed(1) : "?";
    const opt = document.createElement("option");
    opt.value = id;
    opt.textContent = `${id} (~${gb} GB)`;
    modelSelect.appendChild(opt);
  }

  if (available.includes(DEFAULT_MODEL_ID)) {
    modelSelect.value = DEFAULT_MODEL_ID;
  }
}

function addMessage(role, text) {
  const el = document.createElement("div");
  el.className = `msg ${role === "user" ? "user" : "bot"}`;
  el.textContent = text;
  messagesEl.appendChild(el);
  messagesEl.scrollTop = messagesEl.scrollHeight;
  return el;
}

async function loadModel() {
  if (!navigator.gpu) {
    setStatus(
      "This browser doesn't expose WebGPU, so it can't run the model. " +
        "Try a recent Chrome, Edge, or another WebGPU-capable browser.",
      true,
    );
    return;
  }

  const modelId = modelSelect.value;
  loadBtn.disabled = true;
  modelSelect.disabled = true;
  progressWrap.classList.remove("hidden");
  setStatus("");

  try {
    engine = await webllm.CreateMLCEngine(modelId, {
      initProgressCallback: (report) => {
        const pct = Math.round((report.progress || 0) * 100);
        progressFill.style.width = `${pct}%`;
        progressText.textContent = report.text || `${pct}%`;
      },
    });

    setStatus(`Loaded ${modelId}. Ready to chat.`);
    setupSection.classList.add("hidden");
    chatSection.classList.remove("hidden");
    chatInput.focus();
  } catch (err) {
    console.error(err);
    setStatus(`Failed to load model: ${err.message || err}`, true);
    loadBtn.disabled = false;
    modelSelect.disabled = false;
  }
}

async function sendMessage(text) {
  history.push({ role: "user", content: text });
  addMessage("user", text);

  const pendingEl = addMessage("bot", "...");
  pendingEl.classList.add("pending");

  generating = true;
  sendBtn.disabled = true;

  try {
    const stream = await engine.chat.completions.create({
      messages: history,
      stream: true,
    });

    let reply = "";
    for await (const chunk of stream) {
      const delta = chunk.choices?.[0]?.delta?.content;
      if (delta) {
        reply += delta;
        pendingEl.textContent = reply;
        pendingEl.classList.remove("pending");
        messagesEl.scrollTop = messagesEl.scrollHeight;
      }
    }

    history.push({ role: "assistant", content: reply || "" });
  } catch (err) {
    console.error(err);
    pendingEl.classList.remove("pending");
    pendingEl.textContent = `Error: ${err.message || err}`;
    history.pop(); // drop the user turn that failed to get a reply
  } finally {
    generating = false;
    sendBtn.disabled = false;
  }
}

chatForm.addEventListener("submit", (e) => {
  e.preventDefault();
  if (generating) return;
  const text = chatInput.value.trim();
  if (!text) return;
  chatInput.value = "";
  sendMessage(text);
});

chatInput.addEventListener("keydown", (e) => {
  if (e.key === "Enter" && !e.shiftKey) {
    e.preventDefault();
    chatForm.requestSubmit();
  }
});

resetBtn.addEventListener("click", () => {
  history = [{ role: "system", content: SYSTEM_PROMPT }];
  messagesEl.innerHTML = "";
});

loadBtn.addEventListener("click", loadModel);

populateModelSelect();
