module.exports = {
  root: true,
  parser: "@typescript-eslint/parser",
  parserOptions: {
    project: "./tsconfig.json",
    ecmaVersion: "latest",
    ecmaFeatures: {
      jsx: true,
    },
  },
  plugins: ["@typescript-eslint/eslint-plugin", "unicorn", "sonarjs", "eslint-comments", "react"],
  extends: [
    "eslint:recommended",
    "plugin:@typescript-eslint/recommended",
    "plugin:@typescript-eslint/recommended-requiring-type-checking",
    "plugin:unicorn/recommended",
    "plugin:sonarjs/recommended",
    "plugin:eslint-comments/recommended",
    "plugin:react/recommended",
    "plugin:react/jsx-runtime",
    "prettier",
  ],
  ignorePatterns: [".eslintrc.cjs", "vite.config.ts"],
  rules: {
    "unicorn/filename-case": "off",
    "unicorn/prevent-abbreviations": "off",
  },
}
