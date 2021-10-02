import React from "react";
import ReactDOM from "react-dom";
import ListApp from "../../components/ListApp";

ReactDOM.render(
    <React.StrictMode>
        <ListApp name={'りんご'} items={['シナノゴールド', '秋映', 'ふじ']} />
    </React.StrictMode>,
    document.getElementById('root')
)
