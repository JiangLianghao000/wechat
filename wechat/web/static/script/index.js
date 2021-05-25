$("#printContent").emoji({
    //设置触发emojy的按键
    button: "#icon-Ovalx",
    showTab: false,
    animation: 'slide',
    position: 'topLeft',
    icons: [{
        name: "QQ表情",
        path: "static/lib/jquery-emoji/img/qq/",
        maxNum: 91,
        excludeNums: [41, 45, 54],
        file: ".gif"
    }]
})