import React from "react";
import ReactDOM from "react-dom";
import ListApp from "../../components/ListApp";


ReactDOM.render(
    <React.StrictMode>
        <ListApp name={'さつまいも'} items={['紅はるか', 'シルクスイート', 'パープルスイートロード']} />
    </React.StrictMode>,
    document.getElementById('root')
)
