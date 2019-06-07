var currentSelection = 0;

function navigationHandler(keyEvent) {
  var oldSelection = currentSelection;
  var maxSelection = document.getElementById('content').childNodes.length;

  if (keyEvent.keyCode === 37) {
    // left
    if (currentSelection > 2) {
      currentSelection--;
    } else {
      return;
    }
  } else if (keyEvent.keyCode === 38) {
    // up
    if ((currentSelection - 4) > 1) {
      currentSelection -= 4;
    } else {
      return;
    }
  } else if (keyEvent.keyCode === 39) {
    // right
    if ((currentSelection + 1) < maxSelection) {
      currentSelection++;
    } else {
      return;
    }
  } else if (keyEvent.keyCode === 40) {
    // down
    if ((currentSelection + 4) < maxSelection) {
      currentSelection += 4;
    } else {
      return;
    }
  } else {
    // not navigation
    return;
  }
  document.getElementById('command_input').blur();
  if (oldSelection > 0) {
    var oldElement = document.getElementById('content').childNodes[oldSelection];
    if (oldElement != null) {
      oldElement.className = 'box';
    }
  }
  var newElement = document.getElementById('content').childNodes[currentSelection];
  newElement.className = 'box selected';
}

function focusHandler(keyEvent) {
  if (document.activeElement === document.getElementById('command_input')) {
    return;
  }
  if (keyEvent.key === '/') {
    keyEvent.preventDefault();
    document.getElementById('command_input').focus();
  }
}

function commandHandler(keyEvent) {
  if (keyEvent.key === 'Enter') {
    var me = document.getElementById('command_input');
    keyEvent.preventDefault();
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
      if (this.readyState == 4 && this.status == 200) {
        var div = document.createElement('div');
        div.textContent = xhttp.responseText;
        div.className = "box";
        document.getElementById('content').appendChild(div);
        currentSelection = 1;
      }
    };
    xhttp.open("GET", "/command?input=" + me.value, true);
    xhttp.send();
  }
}

// bootstrap
(function () {
  document.onkeypress = focusHandler;
  document.onkeydown = navigationHandler;
  document.getElementById('command_input').onkeypress = commandHandler;
})();
