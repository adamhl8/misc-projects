import { MantineProvider } from "@mantine/core"
import ky from "ky"
import { useEffect, useState } from "react"
import { z } from "zod"
import { theme } from "./theme.js"

const App = () => {
  return (
    <MantineProvider theme={{ colorScheme: "dark" }} withGlobalStyles withNormalizeCSS>
      <h1>:)</h1>
    </MantineProvider>
  )
}

export default App
