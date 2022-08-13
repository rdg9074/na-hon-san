module.exports = {
  env: {
    browser: true,
    es2021: true,
    jest: true
  },
  extends: [
    "airbnb",
    "plugin:react/recommended",
    "plugin:jsx-a11y/recommended",
    "plugin:@typescript-eslint/recommended",
    "plugin:prettier/recommended",
    "plugin:cypress/recommended"
  ],
  parser: "@typescript-eslint/parser",
  parserOptions: {
    ecmaFeatures: {
      jsx: true
    },
    ecmaVersion: "latest",
    sourceType: "module"
  },
  plugins: ["react", "@typescript-eslint", "prettier"],
  rules: {
    "react/jsx-filename-extension": [
      2,
      {
        extensions: [".js", ".jsx", ".ts", ".tsx"]
      }
    ],
    "import/no-unresolved": 0,
    "import/extensions": 0,
    "@typescript-eslint/no-var-requires": 0,
    "prettier/prettier": ["error", { endOfLine: "auto" }],
    // "no-param-reassign": [
    //   "error",
    //   { props: true, ignorePropertyModificationsFor: ["state"] }
    // ]
    "no-param-reassign": 0,
    "@typescript-eslint/no-empty-interface": 0
  },
  settings: {
    "import/resolver": {
      node: {
        extensions: [".ts", ".js", ".jsx", ".json", ".svg"]
      }
    }
  }
};
