import List from "./List";
import Counter from "./Counter";

export type Props = {
    name: string
    items: string[]
}

const ListApp = ({name, items}: Props): JSX.Element => {
    return (
        <>
            <h1>{name}</h1>
            <List items={items} />
            <Counter />
            <p><a href="/">戻る</a></p>
        </>
    )
}
export default ListApp
