import {useState} from "react";

const Component = (): JSX.Element => {
    const [count, setCount] = useState(0)

    return (
        <>
            <button type="button" onClick={() => setCount((count) => count + 1)}>
                へー {count}
            </button>
        </>
    )
}
export default Component
