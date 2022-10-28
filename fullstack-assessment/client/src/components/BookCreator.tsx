import { Button, Grid, Stack, TextInput } from "@mantine/core"
import ky from "ky"
import { FC, useState } from "react"
import { z } from "zod"
import { Books, TBooks, URL } from "../App.js"

interface BookCreatorProps {
  books: TBooks
  setBooks: React.Dispatch<React.SetStateAction<TBooks>>
}

const NewBook = z.object({
  title: z.string().min(1),
  author: z.string().min(1),
})
type TNewBook = z.infer<typeof NewBook>

const BookCreator: FC<BookCreatorProps> = ({ books, setBooks }) => {
  const [newBookTitle, setNewBookTitle] = useState("")
  const [newBookAuthor, setNewBookAuthor] = useState("")

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    void createBook()
  }

  const createBook = async () => {
    const input: TNewBook = {
      title: newBookTitle,
      author: newBookAuthor,
    }

    const response = await ky.post(`${URL}/books`, { json: NewBook.parse(input) }).json()
    setBooks(Books.parse([...books, response]))

    setNewBookTitle("")
    setNewBookAuthor("")
  }

  return (
    <div>
      <Stack align="center">
        <form onSubmit={handleSubmit}>
          <input type="submit" style={{ display: "none" }} />
          <Grid>
            <Grid.Col span={"content"}>
              <TextInput
                value={newBookTitle}
                placeholder="Title"
                label="Book Title"
                radius="md"
                onChange={(event) => setNewBookTitle(event.currentTarget.value)}
              />
            </Grid.Col>
            <Grid.Col span={"content"}>
              <TextInput
                value={newBookAuthor}
                placeholder="Author"
                label="Book Author"
                radius="md"
                onChange={(event) => setNewBookAuthor(event.currentTarget.value)}
              />
            </Grid.Col>
          </Grid>
        </form>
        <Button
          variant="light"
          color="green"
          radius="md"
          size="md"
          onClick={() => {
            void createBook()
          }}
        >
          Add Book
        </Button>
      </Stack>
    </div>
  )
}

export default BookCreator
