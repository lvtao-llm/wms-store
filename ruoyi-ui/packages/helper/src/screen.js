import moment from "moment";
import { downloadData } from "./browser";

const ScreenRecording = {
  isScreenRecording: false,
  isScreenRecordingPause: false,
  inter: {
    screenRecording: -1
  }
};
// 录屏-暂停
function screenRecordingPause() {
  if (ScreenRecording.isScreenRecordingPause) return false;
  ScreenRecording.isScreenRecordingPause = true;
  if (ScreenRecording.recorder) {
    ScreenRecording.recorder.pause();
    ScreenRecording.info("录制已暂停");
  } else ScreenRecording.warn("屏幕记录器尚未初始化，暂不可暂停");
}
// 录屏-继续
function screenRecordingContinue() {
  if (!ScreenRecording.isScreenRecordingPause) return false;
  ScreenRecording.isScreenRecordingPause = false;
  if (ScreenRecording.recorder) {
    ScreenRecording.recorder.resume();
    ScreenRecording.info("录制继续");
  } else ScreenRecording.warn("屏幕记录器尚未初始化，暂不可继续");
}
// 录屏-结束
function screenRecordingStop() {
  ScreenRecording.isScreenRecording = false;
  ScreenRecording.isScreenRecordingPause = false;
  if (ScreenRecording.recorder) {
    ScreenRecording.recordStream.active && ScreenRecording.recordStream.getTracks().forEach(track => track.stop());
    ScreenRecording.isTriggerStopRecordStream = true;
    ScreenRecording.recorder.stop();
  } else console.warn("屏幕记录器尚未初始化，暂不可终止");
}
function screenRecording() {
  if (ScreenRecording.isScreenRecording) {
    console.warn("正在录制屏幕中...");
    return false;
  }
  ScreenRecording.isScreenRecording = true;
  ScreenRecording.isScreenRecordingPause = false;
  clearInterval(ScreenRecording.inter.screenRecording);
  if (navigator.mediaDevices && navigator.mediaDevices.getDisplayMedia) {
    navigator.mediaDevices.getDisplayMedia({
      video: {
        cursor: "always"
      },
      audio: {
        echoCancellation: true,
        noiseSuppression: true,
        sampleRate: 44100
      }
    }).then(stream => {
      // 初始化记录器
      const mimeType = "video/webm";
      if (!MediaRecorder.isTypeSupported(mimeType)) {
        ScreenRecording.isScreenRecording = false;
        console.error(`屏幕记录器再当前浏览器不支持 [ ${mimeType} ] 类型的视频录制`);
        return false;
      }
      ScreenRecording.recorder = new MediaRecorder(stream, { mimeType });
      ScreenRecording.recorder.chunks = [];
      ScreenRecording.recorder.ondataavailable = e => ScreenRecording.recorder.chunks.push(e.data);
      // 屏幕记录器异常处理
      ScreenRecording.recorder.onerror = e => {
        ScreenRecording.isScreenRecording = false;
        console.error(e);
        console.error("屏幕记录器异常，请在浏览器控制台查看详情");
      };
      // 保存录屏
      ScreenRecording.recorder.saveRecord = () => {
        ScreenRecording.isScreenRecordingPause = ScreenRecording.isScreenRecording = false;
        if (ScreenRecording.recorder && ScreenRecording.recorder.chunks) {
          if (ScreenRecording.recorder.chunks.length < 1) {
            console.info("录制时长过短，不可保存");
          } else {
            console.info("已录制完成");
            const blob = new Blob(ScreenRecording.recorder.chunks, { type: mimeType });
            const url = URL.createObjectURL(blob);
            window.open(url, "屏幕录制 ", "_blank");
            downloadData(blob, `录屏-${moment().format("YYYY-MM-DD HH:mm:ss")}.webm`, { isBlob: true });
          }
        } else {
          console.error("屏幕记录器异常，无法保存视频");
        }
        clearInterval(ScreenRecording.inter.screenRecording);
        delete ScreenRecording.recordStream;
        delete ScreenRecording.recorder;
        delete ScreenRecording.isTriggerStopRecordStream;
      };
      // 屏幕记录器停止处理
      ScreenRecording.recorder.onstop = ScreenRecording.recorder.saveRecord.bind(ScreenRecording);
      // 录屏计时
      ScreenRecording.recorder.duration = 0;
      ScreenRecording.recorder.timeHandle = () => {
        if (!ScreenRecording.isScreenRecordingPause) {
          ScreenRecording.recorder.duration += 1e3;
          console.info(`录屏中: ${Math.floor(ScreenRecording.recorder.duration / 1e3)}秒`);
        }
      };
      ScreenRecording.recorder.timeHandle();
      ScreenRecording.inter.screenRecording = setInterval(ScreenRecording.recorder.timeHandle.bind(ScreenRecording), 1e3);
      // 流停止处理
      ScreenRecording.recordStream = stream;
      ScreenRecording.recordStream.oninactive = () => !ScreenRecording.isTriggerStopRecordStream && screenRecordingStop();
      ScreenRecording.recorder.start();
    }).catch(e => {
      const es = e.toString();
      if (es === "NotAllowedError: Permission denied") {
        ScreenRecording.isScreenRecording = false;
        console.warn("您已经取消了录屏");
      } else {
        console.error("录制异常", e, es.toString());
      }
    });
  } else {
    console.warn("当前浏览器暂不支持录屏，请使用最新的谷歌或者Edge或者火狐浏览器");
    ScreenRecording.isScreenRecording = false;
  }
}
export {
  screenRecording,
  screenRecordingPause,
  screenRecordingContinue,
  screenRecordingStop
};