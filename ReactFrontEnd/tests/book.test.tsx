import puppeteer from "puppeteer";

describe("admin UI testing", () => {
  let browser: puppeteer.Browser;
  let page: puppeteer.Page;
  const app = "http://localhost:3000/";
  beforeEach(async () => {
    browser = await puppeteer.launch({
      headless: false,
      slowMo: 20,
    });
    page = await browser.newPage();

    await page.goto(app);
    await page.waitForSelector("#root");
  }, 9000000);
  it("should login", async () => {
    await page.click('button[data-testid="admin-login-button"]');
    await page.waitForSelector('input[type="text"]');
    // await page.type('input[type="text"]', "admin");
    await page.focus('input[type="text"]');
    await page.keyboard.down("Control");
    await page.keyboard.press("A");
    await page.keyboard.up("Control");
    await page.keyboard.press("Backspace");
    await page.keyboard.type("admin");
    await page.focus('input[type="password"]');
    await page.keyboard.down("Control");
    await page.keyboard.press("A");
    await page.keyboard.up("Control");
    await page.keyboard.press("Backspace");
    await page.keyboard.type("Passw0rd");
    await page.click("#login");
    await page.waitForSelector(".MuiListItem-button");
  }, 9000000);

  it("should add layout", async () => {
    const url = page.url();
    console.log(url);
  });

  afterEach(() => {
    browser.close();
  });
});
