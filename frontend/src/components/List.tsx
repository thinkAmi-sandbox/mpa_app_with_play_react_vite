export type Props = {
    items: string[]
}

const Component = ({items}: Props): JSX.Element => {
    return (
        <ul>
            {items.map((item) => (
                <li key={item}>
                    <p>{item}</p>
                </li>
            ))}
        </ul>
    )
}
export default Component
