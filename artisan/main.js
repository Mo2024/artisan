// const {app, BrowserWindow} = require('electron')
// const url = require("url");
// const path = require("path");

// let mainWindow

// function createWindow () {
//   mainWindow = new BrowserWindow({
//     width: 800,
//     height: 600,
//     webPreferences: {
//       nodeIntegration: true
//     }
//   })

//   mainWindow.loadFile('/dist/artisan/index.html');

//   // Open the DevTools.
//   mainWindow.webContents.openDevTools()

//   mainWindow.on('closed', function () {
//     mainWindow = null
//   })
// }

// app.on('ready', createWindow)

// app.on('window-all-closed', function () {
//   if (process.platform !== 'darwin') app.quit()
// })

// app.on('activate', function () {
//   if (mainWindow === null) createWindow()
// })











// const { app, BrowserWindow } = require('electron');
// const url = require('url');
// const path = require('path');
// const { spawn } = require('child_process');

// let mainWindow;

// function createWindow() {
//   mainWindow = new BrowserWindow({
//     width: 800,
//     height: 600,
//     webPreferences: {
//       nodeIntegration: true
//     }
//   });

//   mainWindow.loadURL(
//     url.format({
//       pathname: path.join(__dirname, `/dist/artisan/browser/index.html`),
//       protocol: "file:",
//       slashes: true
//     })
//   );

//   // mainWindow.setMenu(null);

//   // Provide the exact path to app.js
//   const childProcess = spawn('node', [path.join('C:', 'Users', 'Mohamed', 'Desktop', 'artisan', 'backend', 'app.js')]);

//   // Handle child process events
//   childProcess.stdout.on('data', (data) => {
//     console.log(`Child process stdout: ${data}`);
//   });

//   childProcess.stderr.on('data', (data) => {
//     console.error(`Child process stderr: ${data}`);
//   });

//   childProcess.on('close', (code) => {
//     console.log(`Child process exited with code ${code}`);
//   });

//   mainWindow.on('closed', function () {
//     mainWindow = null;
//     // Kill the child process when the main window is closed
//     childProcess.kill();
//   });
// }

// app.on('ready', createWindow);

// app.on('window-all-closed', function () {
//   if (process.platform !== 'darwin') app.quit();
// });

// app.on('activate', function () {
//   if (mainWindow === null) createWindow();
// });








const { app, BrowserWindow } = require('electron')
const url = require('url');
const path = require('path');
let mainWindow
function createWindow () {
  mainWindow = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      nodeIntegration: true
    }
  })
  mainWindow.loadURL(
    url.format({
      pathname: path.join(__dirname, `/dist/artisan/browser/index.html`),
      protocol: "file:",
      slashes: true
    })
  );

    // mainWindow.setMenu(null);

  // mainWindow.webContents.openDevTools()
  mainWindow.on('closed', function () {
    mainWindow = null
  })
}
app.on('ready', createWindow)
app.on('window-all-closed', function () {
  app.quit()
  const { exec } = require('child_process');

  // Run the taskkill command to kill the node.exe process
  exec('taskkill /f /im myapp.exe', (error, stdout, stderr) => {
    if (error) {
      console.error(`Error killing process: ${error}`);
      return;
    }
    console.log(`Node.js process killed successfully`);
  });

})
app.on('activate', function () {
  if (mainWindow === null) createWindow()
})